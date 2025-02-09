package com.sun.media.img.model;

import com.sun.base.bean.MediaFile;

import java.util.ArrayList;

/**
 * @author: Harper
 * @date: 2022/7/19
 * @note: 图片文件夹实体类
 */
public class MediaFolder {

    private int folderId;
    private String folderName;
    private String folderCover;
    private boolean isCheck;
    private ArrayList<MediaFile> mediaFileList;

    public MediaFolder(int folderId, String folderName, String folderCover, ArrayList<MediaFile> mediaFileList) {
        this.folderId = folderId;
        this.folderName = folderName;
        this.folderCover = folderCover;
        this.mediaFileList = mediaFileList;
    }

    public int getFolderId() {
        return folderId;
    }

    public void setFolderId(int folderId) {
        this.folderId = folderId;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderCover() {
        return folderCover;
    }

    public void setFolderCover(String folderCover) {
        this.folderCover = folderCover;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public ArrayList<MediaFile> getMediaFileList() {
        return mediaFileList;
    }

    public void setMediaFileList(ArrayList<MediaFile> mediaFileList) {
        this.mediaFileList = mediaFileList;
    }


}
