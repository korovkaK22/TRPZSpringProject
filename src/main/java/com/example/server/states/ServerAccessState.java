package com.example.server.states;

import com.example.users.ServerUser;
import org.apache.ftpserver.ftplet.FtpSession;
import org.apache.ftpserver.ftplet.FtpletResult;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class ServerAccessState {

    public abstract FtpletResult checkServerAccess(ServerUser user, int maxUsers, AtomicInteger activeConnections);
}
