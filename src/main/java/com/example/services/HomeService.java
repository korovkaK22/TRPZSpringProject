package com.example.services;

import com.example.server.FTPServer;
import com.example.visitor.IVisitor;
import com.example.visitor.ServerInformation;
import lombok.AllArgsConstructor;
import org.apache.ftpserver.FtpServer;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class HomeService {
    IVisitor iVisitor;
    FTPServer ftpServer;

    public ServerInformation getInformation(){
      return iVisitor.getStatistic(ftpServer);
    }
}
