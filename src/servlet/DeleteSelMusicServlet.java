package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.MusicDao;
import entity.Music;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 删除选中的歌曲
 */
@WebServlet("/deleteSelMusicServlet")
public class DeleteSelMusicServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");
        String[] values = req.getParameterValues("id[]");
        MusicDao musicDao = new MusicDao();
        int sum = 0;
        Map<String ,Object> return_map = new HashMap<>();

        for (int i = 0; i < values.length; i++) {
            int id = Integer.parseInt(values[i]);
            Music music = musicDao.findMusicById(id);

            int delete = musicDao.deleteMusicById(id);

            if(delete == 1) {
                File file = new File("/root/apache-tomcat-8.5.57/webapps/onlineMusic/"+music.getUrl()+".mp3");
                if(file.delete()) {
                    sum += delete;
                }else {
                    return_map.put("msg",false);
                    System.out.println("服务器删除失败！");
                }
            }else {
                return_map.put("msg",false);
                System.out.println("数据库删除失败！");
            }

        }

        if(sum == values.length) {
            return_map.put("msg",true);
        }else {
            return_map.put("msg",false);
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(resp.getWriter(),return_map);
    }
}
