package jdragon.club;

import  ConDatebase.*;
import cc.moecraft.icq.PicqBotX;
import cc.moecraft.icq.PicqConfig;

public class robot
{
    public static Long tljGroupNum = 974172771L;
    public static  Long xiaochaiQ = 207938707L;
    public static void main(String[] args)
    {
        // ���������˶��� ( �������� )
        PicqBotX bot = new PicqBotX(new PicqConfig(9999).setDebug(true));

        // ���һ���������˻� ( ����, ����URL, ���Ͷ˿� )
        bot.addAccount("Bot01", "127.0.0.1", 5700);

        // ע���¼�������, ����ע����������
        bot.getEventManager().registerListeners(
                new OneUserNum(),
                new RobotGroupClient()
        );
        // ����ָ�������
        // ��Щ�ַ�����ָ��ǰ׺, ����ָ��"!help"��ǰ׺����"!"
        bot.enableCommandManager("bot -", "!", "/", "~");

        // ע��ָ��, ����ע����ָ��
        bot.getCommandManager().registerCommands(
                new CommandSay(),//�����Լ�����
                new ReturnLast(),//�������һ�����Ѽ�¼
                new ReturnAll(),//���ص���ȫ�����Ѽ�¼
                new CommandVersion(),//���ذ汾
                new AllUserNum()
        );
        // ����������, ����ռ�����߳�
        bot.startBot();
    }
}