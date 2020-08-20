package dao;

import entity.Music;
import util.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 有关音乐的数据库操作
 */
public class MusicDao {
    /**
     * 查询全部歌单
     */
    public  List<Music> findMusic(){
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Music> musicList = new ArrayList<>();
        try {
            String sql = "select * from music";
            connection = DBUtils.getConnection();
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Music music = new Music();
                music.setId(rs.getInt("id"));
                music.setTitle(rs.getString("title"));
                music.setSinger(rs.getString("singer"));
                music.setTime(rs.getDate("time"));
                music.setUrl(rs.getString("url"));
                music.setUserid(rs.getInt("userid"));
                musicList.add(music);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtils.getClose(connection,ps,rs);
        }
        return musicList;
    }

    /**
     * 根据id查找音乐
     */
    public   Music findMusicById(int id){
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Music music = null;
        try {
            String sql = "select  * from music where id=?";
            connection = DBUtils.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1,id);
            rs = ps.executeQuery();
            if(rs.next()) {
                music = new Music();
                music.setId(rs.getInt("id"));
                music.setTitle(rs.getString("title"));
                music.setSinger(rs.getString("singer"));
                music.setTime(rs.getDate("time"));
                music.setUrl(rs.getString("url"));
                music.setUserid(rs.getInt("userid"));
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtils.getClose(connection,ps,rs);
        }
        return music;
    }

    /**
     * str: 歌名
     * 根据关键字查询歌单
     */
    public   List<Music> ifMusic(String str){
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Music> musicList = new ArrayList<>();
        try {
            String sql = "select * from music where title like '%" + str +"%'";
            connection = DBUtils.getConnection();
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Music music = new Music();
                music.setId(rs.getInt("id"));
                music.setTitle(rs.getString("title"));
                music.setSinger(rs.getString("singer"));
                music.setTime(rs.getDate("time"));
                music.setUrl(rs.getString("url"));
                music.setUserid(rs.getInt("userid"));
                musicList.add(music);
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtils.getClose(connection,ps,rs);
        }
        return musicList;
    }

    /**
     * 上传音乐
     */
    public int insert(String title, String singer, String time, String url,
                      int userid) {
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            String sql = "insert into music(title, singer, time, url, userid) values (?,?,?,?,?)";
            connection = DBUtils.getConnection();
            ps = connection.prepareStatement(sql);

            ps.setString(1,title);
            ps.setString(2,singer);
            ps.setString(3,time);
            ps.setString(4,url);
            ps.setInt(5,userid);

            int ret = ps.executeUpdate();
            //返回值代表，当前受影响的行数
            if(ret == 1) {
                return 1;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtils.getClose(connection,ps,null);
        }
        return 0;
    }

    public  int  deleteMusicById(int id) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            String sql = "delete from music where id=?";
            connection = DBUtils.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1,id);
            int ret = ps.executeUpdate();
            if(ret == 1) {
                //删除music表当中的数据成功
                if(findLoveMusicOnDel(id)) {
                    int ret2 = removeLoveMusicOnDel(id);
                    if(ret2 == 1) {
                        return 1;
                    }
                }
                return 1;//表示这首歌没有被添加到lovemusic这张表当中
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtils.getClose(connection,ps,null);
        }
        return 0;
    }

    public  boolean findLoveMusicOnDel(int id) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "select * from lovemusic where music_id=?";
            connection = DBUtils.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1,id);
            rs = ps.executeQuery();
            if(rs.next()) {
                return true;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtils.getClose(connection,ps,null);
        }
        return false;
    }

    public  int removeLoveMusicOnDel(int id) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int ret = 0;
        try {
            String sql = "delete  from lovemusic where music_id=?";
            connection = DBUtils.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1,id);
            ret = ps.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtils.getClose(connection,ps,null);
        }
        return ret;
    }

    /**
     * 添加喜欢的音乐到我的列表当中。
     * 1、在添加之前，需要判断该音乐是否已经被添加过了
     */
    public boolean findMusicByMusicId(int user_id,int musicID) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int ret = 0;
        try {
            String sql = "select * from lovemusic where user_id=? and music_id=?";
            connection = DBUtils.getConnection();
            ps = connection.prepareStatement(sql);

            ps.setInt(1,user_id);
            ps.setInt(2,musicID);

            rs = ps.executeQuery();
            if(rs.next()) {
                return true;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtils.getClose(connection,ps,null);
        }
        return false;
    }

    public  boolean insertLoveMusic(int userId,int musicId) {
        Connection connection = null;
        PreparedStatement ps = null;
        int ret = 0;
        try {
            String sql = "insert into lovemusic(user_id, music_id) VALUES (?,?)";
            connection = DBUtils.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1,userId);
            ps.setInt(2,musicId);
            ret = ps.executeUpdate();
            if(ret == 1) {
                return true;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtils.getClose(connection,ps,null);
        }
        return false;
    }

    public  int removeLoveMusic(int userId,int musicId) {
        Connection connection = null;
        PreparedStatement ps = null;
        int ret = 0;
        try {
            String sql = "delete from lovemusic where user_id=? and music_id=?";
            connection = DBUtils.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1,userId);
            ps.setInt(2,musicId);
            ret = ps.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtils.getClose(connection,ps,null);
        }
        return ret;
    }

    /**
     * 查询当前用户喜欢的所有音乐
     */
    public  List<Music> findLoveMusic(int user_id){
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<Music> musicList = new ArrayList<>();

        try {
            String sql = "select m.id as music_id,title,singer,time,url,userid from lovemusic lm,music m where lm.music_id=m.id and user_id=?";
            connection = DBUtils.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1,user_id);
            rs = ps.executeQuery();

            while (rs.next()) {
                Music music = new Music();
                music.setId(rs.getInt("music_id"));
                music.setTitle(rs.getString("title"));
                music.setSinger(rs.getString("singer"));
                music.setTime(rs.getDate("time"));
                music.setUrl(rs.getString("url"));
                music.setUserid(rs.getInt("userid"));
                musicList.add(music);
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtils.getClose(connection,ps,null);
        }
        return musicList;
    }


    /**
     * 根据关键字查询喜欢的歌单
     */
    public  List<Music> ifMusicLove(String str,int user_id){
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<Music> musicList = new ArrayList<>();

        try {
            String sql = "select m.id as music_id,title,singer,time,url,userid from lovemusic lm,music m " +
                    "where lm.music_id=m.id and user_id=? and title like '%"+str+"%'";
            connection = DBUtils.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1,user_id);
            rs = ps.executeQuery();

            while (rs.next()) {
                Music music = new Music();
                music.setId(rs.getInt("music_id"));
                music.setTitle(rs.getString("title"));
                music.setSinger(rs.getString("singer"));
                music.setTime(rs.getDate("time"));
                music.setUrl(rs.getString("url"));
                music.setUserid(rs.getInt("userid"));
                musicList.add(music);
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtils.getClose(connection,ps,null);
        }
        return musicList;
    }




    public static void main(String[] args) {

    }

}