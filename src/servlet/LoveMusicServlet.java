package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.MusicDao;
import entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 添加到喜欢中
 */
@WebServlet("/loveMusicServlet")
public class LoveMusicServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");


        String idStr = req.getParameter("id");
        int musicId = Integer.parseInt(idStr);

        User user = (User) req.getSession().getAttribute("user");
        int user_id = user.getId();

        MusicDao musicDao = new MusicDao();

        boolean effect = musicDao.findMusicByMusicId(user_id,musicId);

        Map<String ,Object> return_map = new HashMap<>();

        if(effect) {
            return_map.put("msg",false);
            System.out.println("已经在喜欢列表");
        }else {
            boolean flg = musicDao.insertLoveMusic(user_id,musicId);
            if(flg) {
                return_map.put("msg",true);
                System.out.println("添加成功");
            }else {
                return_map.put("msg",false);
            }
        }


        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(resp.getWriter(),return_map);

    }
}
