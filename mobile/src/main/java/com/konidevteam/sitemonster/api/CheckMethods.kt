package com.konidevteam.sitemonster.api

import android.os.AsyncTask
import java.io.OutputStreamWriter
import java.lang.Exception
import java.net.*
import java.util.concurrent.TimeUnit


/*
 * Copyright (C) 2018 Koni Dev Team, All Rights Reserved
 * https://github.com/KoniDevTeam/SiteMonsterAndroid/
 *
 * This file is part of Site Monster.
 *
 * Site Monster is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Site Monster is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Site Monster.  If not, see <https://www.gnu.org/licenses/>.
 */

private data class HTTPResponse (val responseBody: String, val responseCode: Int)

private data class HTTPRequest (val url: String, val requestBody: String, val httpMethod: String,
                                val useProxy: Boolean = false, val httpHeaders: Map<String, String>, val proxyIp: String = "", val proxyPort: Int = 0,
                                val proxyLogin: String = "", val proxyPassword: String = "")

private class MakeHttpRequestTask : AsyncTask<HTTPRequest, Void, HTTPResponse>() {
    private fun startConnectionWithProxy(req: HTTPRequest, url: URL): HttpURLConnection {
        if (req.useProxy && req.proxyLogin != "") {
            val authenticator = object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(req.proxyLogin, req.proxyPassword.toCharArray())
                }
            }
            Authenticator.setDefault(authenticator)
        }

        return if (req.useProxy)
            url.openConnection(Proxy(Proxy.Type.HTTP, InetSocketAddress(req.proxyIp, req.proxyPort))) as HttpURLConnection
        else
            url.openConnection() as HttpURLConnection
    }

    private fun addReqBody(req: HTTPRequest, connection: HttpURLConnection) {
        if (req.requestBody != "") {
            connection.doOutput = true
            val os = connection.outputStream
            val osw = OutputStreamWriter(os, "UTF-8")
            osw.write(req.requestBody)
            osw.flush()
            osw.close()
            os.close()
        }
    }

    private fun sendRequest(connection: HttpURLConnection): HTTPResponse {
        var response: HTTPResponse
        try {
            val data = connection.inputStream.bufferedReader().readText()
            response = HTTPResponse(data, connection.responseCode)
        } finally {
            connection.disconnect()
        }

        return response
    }

    override fun doInBackground(vararg params: HTTPRequest?): HTTPResponse {
        val req = params[0]
        val url = URL(req!!.url)
        val connection = startConnectionWithProxy(req, url)

        connection.requestMethod = req.httpMethod

        for ((name, value) in req.httpHeaders)
            connection.setRequestProperty(name, value)

        addReqBody(req, connection)

        return sendRequest(connection)
    }

    /**
     * Checks website response and accessibility
     * @param req - http request template for website
     * @param responseBody - expected response body
     * @param responseCodes - array of expected response codes
     * @param timeout - request timeout in milliseconds
     */
    fun checkWebsite(req: HTTPRequest, responseBody: String, responseCodes: Array<Int>, timeout: Long): Boolean {
        try {
            val HttpReqTask = MakeHttpRequestTask()
            HttpReqTask.execute(req)
            val res = HttpReqTask.get(timeout, TimeUnit.MILLISECONDS)
            return res.responseCode in responseCodes && responseBody.equals(res.responseBody)
        } catch (e: Exception) {
            return false
        }
    }
}