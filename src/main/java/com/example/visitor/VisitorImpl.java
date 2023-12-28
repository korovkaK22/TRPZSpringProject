package com.example.visitor;

import com.example.server.FTPServer;


import com.example.users.ServerUser;
import org.apache.ftpserver.ftplet.FtpStatistics;
import org.apache.ftpserver.impl.DefaultFtpServer;
import org.apache.ftpserver.impl.FtpServerContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class VisitorImpl implements IVisitor {
    @Override
    public ServerInformation getStatistic(FTPServer ftpServer) {
        DefaultFtpServer server = ftpServer.getServer();
        FtpServerContext ftpServerContext = server.getServerContext();
        ServerInformation result = new ServerInformation();
        FtpStatistics statistics = ftpServerContext.getFtpStatistics();

        result.setMaxUsersNumber(ftpServer.getMaxUsers());
        result.setCurrentUsersNumber(statistics.getCurrentLoginNumber());
        result.setAllOnlineUsersName(ftpServer.getActiveUsers().stream().map(ServerUser::getName).toList());
        result.setAllUsersName(Arrays.stream(ftpServer.getUserManager().getAllUserNames()).toList());

        return result;
    }
}
