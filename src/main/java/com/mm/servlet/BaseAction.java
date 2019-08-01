package com.mm.servlet;

import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;


public class BaseAction extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			try {
				String method=request.getParameter("action");
				if(method==null || method.isEmpty()){
					method="execute";
				}
				Class clazz=this.getClass();
				Method m=clazz.getMethod(method, HttpServletRequest.class,HttpServletResponse.class);
				String path=(String) m.invoke(this, request,response);
				if(path!=null && ! path.isEmpty()){
					if(path.startsWith("redirect")) {
						String uri=StringUtils.substringAfterLast(path, "redirect:");
						response.sendRedirect(uri);
					}else {
						request.getRequestDispatcher(path).forward(request, response);
					}
				}
			}catch (NoSuchMethodException e) {
				request.getRequestDispatcher("/frame/404.jsp").forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}


	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			doGet(request, response);
	}

	public String execute(HttpServletRequest request, HttpServletResponse response) {
		return "/frame/404.jsp";
	}

}
