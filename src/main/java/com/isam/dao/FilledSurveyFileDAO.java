package com.isam.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.isam.bean.SurveyFile;
import com.isam.helper.SQL;

public class FilledSurveyFileDAO
{
	
	public Map<Integer, Map<String, SurveyFile>> getAll()
	{
		Map<Integer, Map<String, SurveyFile>> result = null;
		SQL sqltool = new SQL();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = sqltool.prepare("SELECT id, year, type, file_title FROM FilledSurveyFile WHERE disable=0 order by year DESC, type ASC");
			rs = pstmt.executeQuery();
			result = new TreeMap<>();
			while (rs.next()) {
				
				int year = rs.getInt("year");
				String type = rs.getString("type");
				SurveyFile bean = new SurveyFile();
				bean.setId(rs.getInt("id"));
				bean.setYear(year);
				bean.setType(type);
				bean.setFile_title(rs.getString("file_title"));
				Map<String, SurveyFile> yearmap = result.getOrDefault(year, new TreeMap<String, SurveyFile>());
				yearmap.put(type, bean);
				
				result.put(year, yearmap);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			try {
				sqltool.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public void insertSurveyFile(SurveyFile bean){
		SQL sqltool = new SQL();
		PreparedStatement pstmt = null;
		try {
			pstmt = sqltool.prepare("INSERT INTO FilledSurveyFile(year,type,file_title,file_content,disable,updateTime) VALUES(?,?,?,?,?,GETDATE())");
			int column = 1;
			pstmt.setInt(column++, bean.getYear());
			pstmt.setString(column++, bean.getType());
			pstmt.setString(column++, bean.getFile_title());
			pstmt.setBytes(column++, bean.getFile_content());
			pstmt.setBoolean(column++, bean.isDisable());
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void updateSurveyFile(SurveyFile bean){
		SQL sqltool = new SQL();
		PreparedStatement pstmt = null;
		try {
			pstmt = sqltool.prepare("UPDATE FilledSurveyFile set "
								  + "file_title = ?,"
								  + "file_content = ?,"
								  + "updateTime = GETDATE()"
								  + "WHERE id = ? ");
			int column = 1;
			pstmt.setString(column++, bean.getFile_title());
			pstmt.setBytes(column++, bean.getFile_content());
			pstmt.setInt(column++, bean.getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			try {
				sqltool.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void deleteFile(int id)
	{
		SQL sqltool = new SQL();
		PreparedStatement pstmt = null;
		try {
			pstmt = sqltool.prepare("UPDATE FilledSurveyFile set "
								  + "disable = ? "
								  + "WHERE id = ? ");
			int column = 1;
			pstmt.setBoolean(column++, true);
			pstmt.setInt(column++, id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			try {
				sqltool.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean isExist(int year, String type)
	{
		boolean result = false;
		SQL sqltool = new SQL();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = sqltool.prepare("SELECT year, type FROM FilledSurveyFile WHERE year=? AND type=? AND disable=0");
			int column = 1;
			pstmt.setInt(column++, year);
			pstmt.setString(column++, type);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			try {
				sqltool.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
