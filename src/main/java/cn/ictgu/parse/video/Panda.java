package cn.ictgu.parse.video;

import cn.ictgu.bean.response.Video;
import cn.ictgu.constant.ExceptionEnum;
import cn.ictgu.exception.AnyException;
import cn.ictgu.parse.Parser;
import cn.ictgu.bean.response.Episode;
import cn.ictgu.tools.JsoupUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.nodes.Document;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 熊猫TV
 * Created by Silence on 2017/4/30.
 */
public class Panda implements Parser<Video>{
    private static final String PANDA_API = "http://room.api.m.panda.tv/index.php?method=room.shareapi&roomid=";

    @Override
    public Video parse(String url) {
      final Video video = new Video();
      video.setValue(url);
      String roomId = this.getRoomId(url);
      Document document = JsoupUtils.getDocWithPhone(PANDA_API + roomId);
      JSONObject json = JSON.parseObject(document.body().text());
      JSONObject data = json.getJSONObject("data");
      video.setTitle(data.getJSONObject("roominfo").getString("name"));
      video.setImage(data.getJSONObject("roominfo").getJSONObject("pictures").getString("img").replace("http:",""));
      video.setPlayUrl(data.getJSONObject("videoinfo").getString("address"));
      return video;
    }

    @Override
    public List<Episode> parseEpisodes(String url) {
      return null;
    }

      /**
       * 从 URL 中获取房间号
       */
    private String getRoomId(String videoUrl){
      Matcher matcher = Pattern.compile("([0-9]{3,})").matcher(videoUrl);
      if (matcher.find()){
        return matcher.group(1);
      }
      throw new AnyException(ExceptionEnum.VID_CANNOT_MATCH);
    }

}
