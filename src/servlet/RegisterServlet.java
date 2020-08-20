package servlet;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.UserDao;
import entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
@WebServlet("/registerServlet")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=utf-8");
        String nameStr = req.getParameter("username");
        String password = req.getParameter("password");
        String eamilStr = req.getParameter("email");
        String ageStr = req.getParameter("age");
        int age = Integer.parseInt(ageStr);
        String genderStr = req.getParameter("gender");
        User user = new User();
        user.setUsername(nameStr);
        user.setPassword(password);
        user.setEmail(eamilStr);
        user.setGender(genderStr);
        user.setAge(age);
        UserDao dao = new UserDao();
        Map<String,Object> return_map = new HashMap<>();
        if ((dao.findUserByUsername(user.getUsername()) == null) && (dao.register(user) == 1)){
            return_map.put("msg",true);
        }else {

             //   System.out.println("登录成功");
                return_map.put("msg", false);

        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(resp.getWriter(),return_map);
    }
}