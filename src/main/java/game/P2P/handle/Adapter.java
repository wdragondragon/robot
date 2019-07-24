package game.P2P.handle;

import cc.moecraft.icq.sender.IcqHttpApi;
import cn.zhouyafeng.itchat4j.api.MessageTools;
import game.P2P.Tools.DownloadMsg;

import java.text.SimpleDateFormat;
import java.util.Date;

class Adapter {
    private IcqHttpApi httpAPI;
    Adapter(IcqHttpApi httpAPI){
        this.httpAPI = httpAPI;
    }
    //从QQ发送文本信息
    void SendMsgToID(String message,
                            String id,
                            String type){
        if(type.equals("QQ")){
            httpAPI.sendPrivateMsg(Long.valueOf(id),message);
        }else if(type.equals("WX")){
            int imageindex = message.indexOf("url=");
            int recordindex = message.indexOf("record,file=");
            //向微信发送图片
            if(imageindex!=-1){
                message = message.substring(imageindex+4,message.length()-1);
                String filename = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date())+".jpg";
//                String image = "C:\\Users\\Lenovo\\Desktop\\酷Q Pro\\data\\image\\"+ filename;
                String image = "/root/coolq/data/image/"+filename;
                DownloadMsg.downloadMsg(message,image);
                MessageTools.sendPicMsgByUserId(id,image);
            }else if(recordindex!=-1){
                //向微信发送语音
                String filename = message.substring(recordindex+12,message.length()-1);
//                String voice = "C:\\Users\\Lenovo\\Desktop\\酷Q Pro\\data\\record\\" + filename;
                    String voice = "/root/coolq/data/record/" + filename;
                MessageTools.sendFileMsgByUserId(id,voice);
            } else {
                MessageTools.sendMsgById(message,id);
            }
        }
    }
    //从微信发送图片消息
    void SendWXPicMsgToID(String filepath,
                               String id,
                               String type){
        if(type.equals("QQ")){
            httpAPI.sendPrivateMsg(Long.valueOf(id),"[CQ:image,file=" + filepath + "]");
        }else if(type.equals("WX")){
            String image = "/root/coolq/data/image/"+filepath;
            MessageTools.sendPicMsgByUserId(id,image);
        }
    }
    //从微信发送WX语音消息
    void SendWXVoiceToID(String filepath,
                       String id,
                       String type){
        if(type.equals("QQ")){
            httpAPI.sendPrivateMsg(Long.valueOf(id),"[CQ:record,file=" + filepath + "]");
        }else if(type.equals("WX")){
            MessageTools.sendFileMsgByUserId(id,filepath);
        }
    }
    //从微信转发其他文件，由于其他文件无法从微信转发到QQ，只转发微信
    void SendWXFileMsgToID(String filepath,
                         String id,
                         String type){
        if(type.equals("QQ")){
//            httpAPI.sendPrivateMsg(Long.valueOf(id),"[CQ:image,file=" + filepath + "]");
        }else if(type.equals("WX")){
            MessageTools.sendFileMsgByUserId(id,filepath);
        }
    }
}
