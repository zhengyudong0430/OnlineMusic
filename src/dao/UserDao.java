package dao;
import entity.User;

import util.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 有关用户相关的数据库操作
 */
public  class UserDao {
    /**
     * 登录
     */
    public  User login(User loginUser) {
        User user = null;
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "select * from user where username=? and password=?";
            connection = DBUtils.getConnection();
            ps = connection.prepareStatement(sql);//对SQL语句的预编译
            ps.setString(1,loginUser.getUsername());
            ps.setString(2,loginUser.getPassword());
            //执行sql语句
            rs = ps.executeQuery();
            if(rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setAge(rs.getInt("age"));
                user.setGender(rs.getString("gender"));
                user.setEmail(rs.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtils.getClose(connection,ps,rs);
        }
        return user;
    }
    public User findUserByUsername (String name) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet set = null;
        User user = null;
        try {
            connection = DBUtils.getConnection();
            String sql = "select * from user where username = ? ";
            statement = connection.prepareStatement(sql);
            statement.setString(1,name);
            set = statement.executeQuery();
            if (set.next()) {
                user = new User();
                user.setId(set.getInt("id"));
                user.setUsername(set.getString("username"));
                user.setPassword(set.getString("password"));
                user.setAge(set.getInt("age"));
                user.setGender(set.getString("gender"));
                user.setEmail(set.getString("email"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            DBUtils.getClose(connection,statement,set);
        }
        return user;
    }
    public int register (User user) {
        Connection connection = DBUtils.getConnection();
        PreparedStatement statement = null;
        int ret = 0;
        try {
            String sql = "insert into user values (null,?,?,?,?,?) ";
            statement = connection.prepareStatement(sql);
            statement.setString(1,user.getUsername());
            statement.setString(2,user.getPassword());
            statement.setInt(3,user.getAge());
            statement.setString(4,user.getGender());
            statement.setString(5,user.getEmail());
            ret = statement.executeUpdate();
            if (ret != 1) {
                System.out.println("注册失败");
                return 0;
            }
            System.out.println("注册成功");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            DBUtils.getClose(connection,statement,null);
        }
        return ret;
    }

}



