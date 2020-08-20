package service;

import dao.MusicDao;
import entity.Music;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: GAOBO
 * Date: 2020-07-27
 * Time: 19:49
 */
public class MusicService {

    /*public  int  deleteMusicById(int id) {
        //在这里面你可以将他们嵌套，因为这本身就是逻辑
        MusicDao musicDao = new MusicDao();
        if(musicDao.deleteMusicById(id) == 1) {
            if(musicDao.findLoveMusicOnDel(id)){

            }
        }
    }*/
    public List<Music> findMusic(){
        MusicDao musicDao = new MusicDao();
        List<Music> musicList = musicDao.findMusic();
        return musicList;
    }
}