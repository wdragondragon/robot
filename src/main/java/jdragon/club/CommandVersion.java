package jdragon.club;

import cc.moecraft.icq.command.CommandProperties;
import cc.moecraft.icq.command.interfaces.EverywhereCommand;
import cc.moecraft.icq.event.events.message.EventMessage;
import cc.moecraft.icq.user.User;

import java.util.ArrayList;

public class CommandVersion implements EverywhereCommand // ʵ��EverywhereCommand��������˽��Ⱥ�Ļ��������鶼���յ���ָ��
{
    // ָ������

    public CommandProperties properties()
    {
        // �����������ָ��������������, ָ��������������һ��
        // ����Ļ�, ��"!v", "!version", ��"!�汾"���ܴ���ָ�� (��̾��Ϊ�����õ�ǰ׺, ��һ������Ҫ��̾��)
        return new CommandProperties("version", "v", "�汾");
    }

    // �����˽ӵ�ָ����ִ��������� ( ʵ�ֲ�ͬ�ĽӿڵĻ���������һ��һ�� )

    public String run(EventMessage event, User sender, String command, ArrayList<String> args)
    {
        // ����, ����ֵ���Զ��ظ���ȥ
        // ������Ϊ���ָ����������汾��, ����ֱ�ӷ����ַ�����
        return "���������°汾1.700\n���ð汾1.50-1.700";
    }
}