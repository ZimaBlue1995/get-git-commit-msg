package com.github.win15.getgitcommitmsg.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @version : 1.0
 * @auther : 齐马的作品
 * @date : 2022/7/7 1:17
 * @description:
 */
public class GitAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        String gitCommitMsg = getGitCommitMsgByDate(e);
        Messages.showMessageDialog(e.getProject(), gitCommitMsg, "GIT COMMIT MSG", Messages.getInformationIcon());
    }

    private String getGitCommitMsgByDate(AnActionEvent e) {
        Project project = e.getProject();
        String basePath = project.getBasePath();

        // 获取当天日期
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime min = now.with(LocalTime.MIN);
        LocalDateTime max = now.with(LocalTime.MAX);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String minFormat = dateTimeFormatter.format(min);
        String maxFormat = dateTimeFormatter.format(max);


        String userCommand = "git config --get user.name";
        String user = execCMD(userCommand);
        String pathCommand = "cd /d " + basePath;
        String msgCommand = "git log --after=\"" + minFormat + "\" --before=\"" + maxFormat + "\" --author=\"" + user + "\" --pretty=format:%s --no-merges --reverse";

//        String msg = execCMD(msgCommand);
        String msg = execCMDWithPath(pathCommand, msgCommand);

        String[] split = msg.split("\\r?\\n");

        List<String> collect = Arrays.stream(split)
                .collect(Collectors.toList());

        System.out.println("msg=" + msg);
        System.out.println("collect = " + collect);
        return msg;
    }

    //执行cmd命令，获取返回结果
    public static String execCMD(String command) {
        StringBuilder sb = new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (Exception e) {
            return e.toString();
        }
        return sb.toString();
    }

    public static String execCMDWithPath(String path, String command) {
        StringBuilder sb = new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec(path + "&&" + command);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (Exception e) {
            return e.toString();
        }
        return sb.toString();
    }
}
