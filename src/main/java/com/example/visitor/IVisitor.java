package com.example.visitor;

import com.example.server.FTPServer;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.impl.DefaultFtpServer;

public interface IVisitor {

    ServerInformation getStatistic(FTPServer server);
}
