package com.example.visitor;

import com.example.server.FTPServer;

public interface IVisitor {

    ServerInformation getStatistic(FTPServer server);
}
