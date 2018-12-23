package com.konidevteam.sitemonster.api

import android.os.AsyncTask
import java.net.HttpURLConnection
import java.net.URL

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
                                val useProxy: Boolean = false, val http, val proxyIp: String = "", val proxyPort: Int = 0,
                                val proxyLogin: String = "", val proxyPassword: String = "")