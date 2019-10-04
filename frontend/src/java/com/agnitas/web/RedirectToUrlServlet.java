/*

    Copyright (C) 2019 AGNITAS AG (https://www.agnitas.org)

    This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
    You should have received a copy of the GNU Affero General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.

*/

package com.agnitas.web;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class RedirectToUrlServlet extends HttpServlet {
	/** The logger. */
	private static final transient Logger logger = Logger.getLogger(RedirectToUrlServlet.class);

	/** Serial version UID: */
	private static final long serialVersionUID = -595094416663851734L;

	private String destinationUrl = null;
	private int httpCode = 301;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		try {
			destinationUrl = config.getInitParameter("destinationUrl");
		} catch (NumberFormatException e) {
			logger.error("Invalid destinationUrl");
		}

		try {
			httpCode = Integer.parseInt(config.getInitParameter("httpCode"));
		} catch (NumberFormatException e) {
			logger.error("Invalid httpCode");
		}
	}

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		getInitParameter("destinationUrl");
		response.setStatus(httpCode);
		response.setContentType("text/html");
		response.setHeader("Location", destinationUrl);
	}
}
