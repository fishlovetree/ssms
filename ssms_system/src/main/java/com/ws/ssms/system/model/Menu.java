package com.ws.ssms.system.model;

import java.io.Serializable;

public class Menu implements Serializable {
	private static final long serialVersionUID = 1L;
	
    private Integer id;

    private String menuname;

    private String menuenname;

    private String menuurl;

    private Integer menuorder;

    private Integer superid;

    private String menuicon;

    private Integer status;
    
    private Integer menutype;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMenuname() {
        return menuname;
    }

    public void setMenuname(String menuname) {
        this.menuname = menuname == null ? null : menuname.trim();
    }

    public String getMenuenname() {
        return menuenname;
    }

    public void setMenuenname(String menuenname) {
        this.menuenname = menuenname == null ? null : menuenname.trim();
    }

    public String getMenuurl() {
        return menuurl;
    }

    public void setMenuurl(String menuurl) {
        this.menuurl = menuurl == null ? null : menuurl.trim();
    }

    public Integer getMenuorder() {
        return menuorder;
    }

    public void setMenuorder(Integer menuorder) {
        this.menuorder = menuorder;
    }

    public Integer getSuperid() {
        return superid;
    }

    public void setSuperid(Integer superid) {
        this.superid = superid;
    }

    public String getMenuicon() {
        return menuicon;
    }

    public void setMenuicon(String menuicon) {
        this.menuicon = menuicon == null ? null : menuicon.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

	public Integer getMenutype() {
		return menutype;
	}

	public void setMenutype(Integer menutype) {
		this.menutype = menutype;
	}
}