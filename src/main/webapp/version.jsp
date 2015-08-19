<%@page contentType="text/plain"%>
<%@page import="java.net.InetAddress"%>
dpmmc.version=${version}
dpmmc.hostaddress=<%=InetAddress.getLocalHost().getHostAddress() %>
dpmmc.canonicalhostname=<%=InetAddress.getLocalHost().getCanonicalHostName() %>
dpmmc.hostname=<%=InetAddress.getLocalHost().getHostName() %>
dpmmc.tomcat.version=<%= application.getServerInfo() %>
dpmmc.tomcat.catalina_base=<%= System.getProperty("catalina.base") %>
