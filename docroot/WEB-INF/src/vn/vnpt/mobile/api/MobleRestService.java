/**
 * Author: PhiNT
 * Create date: 04/07/2019
 */
package vn.vnpt.mobile.api;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import vn.dtt.cmon.dao.beans.thongtinthutuc.ThongTinThuTuc;
import vn.dtt.cmon.dao.cc.model.CongChuc;
import vn.dtt.cmon.dao.cd.model.CongDan;
import vn.dtt.cmon.dao.cd.service.CongDanLocalServiceUtil;
import vn.dtt.cmon.dao.hosohcc.model.HoSoTTHCCong;
import vn.dtt.cmon.dao.hosohcc.service.HoSoTTHCCongLocalServiceUtil;
import vn.dtt.cmon.dao.qlhc.service.WorkListLocalServiceUtil;
import vn.vnpt.cmon.dao.core.model.ThongKeObject;
import vn.vnpt.cmon.dao.core.service.ThongKeObjectLocalServiceUtil;


import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;


@Path("/")
public class MobleRestService {
	
	private static Log log = LogFactoryUtil.getLog(MobleRestService.class);

	@GET
	@Path("v1/health")
	@Produces("application/json")
	@Consumes("application/json")
	public String health(){
		JSONObject jSonData = new JSONFactoryUtil().createJSONObject();
		jSonData.put("ErrorCode","0");
		jSonData.put("Message", "Server hoạt động");
		//
		return jSonData.toString();
	}
	
	@GET
	@Path("v1/getDashboard")
	@Produces("application/json")
	@Consumes("application/json")
	public String getDashboard(
				@QueryParam("Username") String username, 
				@QueryParam("Password") String password) throws SystemException, NumberFormatException, PortalException {
		log.info("Username: " + username + ", password: " + password);
		JSONObject rs = new JSONFactoryUtil().createJSONObject();
		if( checkValidParam(username, password)){
			// Check
			if( AuthUtil.hasAccess(username, password)){
				// Add data
				rs.put("ErrorCode","0");
				rs.put("Message", "Thống kê tổng hợp hồ sơ một cửa");
				rs.put("TongHop", JsonUtil.ThongKeTongHop());
				rs.put("SoBanNganh", JsonUtil.ThongKeSoBanNganh());
				rs.put("HuyenTP", JsonUtil.ThongKeQuanHuyen());
				rs.put("XaPhuong", JsonUtil.ThongKeXaPhuong());

			} else{
				rs.put("ErrorCode","2");
				rs.put("Message", "Truy cập không hợp lệ. Kiểm tra lại tên đăng nhập và mật khẩu");
			}
	
		} else {
			rs.put("ErrorCode","1");
			rs.put("Message", "Tham số không đủ hoặc tham số không hợp lệ");
		}
		
		return rs.toString();
	}

	
	/**
	 * Kiểm tra tham số truyền vào
	 * @param avgs
	 * @return
	 */
	private boolean checkValidParam(Object ...avgs){
		for(int i = 0; i < avgs.length; i++){
			Object obj = avgs[i];
			if(obj instanceof String){
				if( avgs[i] == null || (String)avgs[i] == "") return false;
			}else if (obj instanceof Integer){
				if( avgs[i] == null ) return false;
			}else{
				if( avgs[i] == null ) return false;
			}
		}
		return true;
	}

	@GET
	@Path("v1/ThongKeTheoThang")
	@Produces("application/json")
	@Consumes("application/json")
	public String ThongKeTheoThang(
			@QueryParam("Username") String username, 
			@QueryParam("Password") String password,
			@QueryParam("CoQuanId") Integer CoQuanId) throws SystemException, NumberFormatException, PortalException {
		log.info("Username: " + username + ", password: " + password + " CoQuanId: " + CoQuanId);
		
		JSONObject jSonData = new JSONFactoryUtil().createJSONObject();
		
		if( checkValidParam(username, password, CoQuanId)){
			// Check
			if( AuthUtil.hasAccess(username, password)){
				jSonData = JsonUtil.ThongKeTheoThang(CoQuanId);
				
			} else{
				jSonData.put("ErrorCode","2");
				jSonData.put("Message", "Truy cập không hợp lệ. Kiểm tra lại tên đăng nhập và mật khẩu");
			}			
		}else{
			jSonData.put("ErrorCode","1");
			jSonData.put("Message", "Tham số không đủ hoặc không hợp lệ");
		}
		return jSonData.toString();
	}
	
	@GET
	@Path("v1/ThongKeTheoTuan")
	@Produces("application/json")
	@Consumes("application/json")
	public String ThongKeTheoTuan(
			@QueryParam("Username") String username, 
			@QueryParam("Password") String password,
			@QueryParam("CoQuanId") Integer CoQuanId) throws SystemException, NumberFormatException, PortalException {
		log.info("Username: " + username + ", password: " + password + " CoQuanId: " + CoQuanId);
		
		JSONObject jSonData = new JSONFactoryUtil().createJSONObject();
		
		if( checkValidParam(username, password, CoQuanId)){
			// Check
			if( AuthUtil.hasAccess(username, password)){
				jSonData = JsonUtil.ThongKeTheoTuan(CoQuanId);
				
			} else{
				jSonData.put("ErrorCode","2");
				jSonData.put("Message", "Truy cập không hợp lệ. Kiểm tra lại tên đăng nhập và mật khẩu");
			}			
		}else{
			jSonData.put("ErrorCode","1");
			jSonData.put("Message", "Tham số không đủ hoặc không hợp lệ");
		}
		return jSonData.toString();
	}
	
	@GET
	@Path("v1/ThongKeHomNay")
	@Produces("application/json")
	@Consumes("application/json")
	public String ThongKeHomNay(
			@QueryParam("Username") String username, 
			@QueryParam("Password") String password,
			@QueryParam("CoQuanId") Integer CoQuanId) throws SystemException, NumberFormatException, PortalException {
		log.info("Username: " + username + ", password: " + password + " CoQuanId: " + CoQuanId);
		
		JSONObject jSonData = new JSONFactoryUtil().createJSONObject();
		
		if( checkValidParam(username, password, CoQuanId)){
			// Check
			if( AuthUtil.hasAccess(username, password)){
				jSonData = JsonUtil.ThongKeHomNay(CoQuanId);
				
			} else{
				jSonData.put("ErrorCode","2");
				jSonData.put("Message", "Truy cập không hợp lệ. Kiểm tra lại tên đăng nhập và mật khẩu");
			}			
		}else{
			jSonData.put("ErrorCode","1");
			jSonData.put("Message", "Tham số không đủ hoặc không hợp lệ");
		}
		return jSonData.toString();
	}
	
	@GET
	@Path("v1/DanhSachCQQLSoBanNganh")
	@Produces("application/json")
	@Consumes("application/json")
	public String DanhSachCQQLSoBanNganh(
			@QueryParam("Username") String username, 
			@QueryParam("Password") String password) throws SystemException, NumberFormatException, PortalException {
		log.info("Username: " + username + ", password: " + password);
		
		JSONObject jSonData = new JSONFactoryUtil().createJSONObject();
		
		if( checkValidParam(username, password)){
			// Check
			if( AuthUtil.hasAccess(username, password)){
				jSonData = JsonUtil.DanhSachCQQL(5);
				
			} else{
				jSonData.put("ErrorCode","2");
				jSonData.put("Message", "Truy cập không hợp lệ. Kiểm tra lại tên đăng nhập và mật khẩu");
			}			
		}else{
			jSonData.put("ErrorCode","1");
			jSonData.put("Message", "Tham số không đủ hoặc không hợp lệ");
		}
		return jSonData.toString();
	}
	
	@GET
	@Path("v1/DanhSachCQQLHuyenXa")
	@Produces("application/json")
	@Consumes("application/json")
	public String DanhSachCQQLHuyenXa(
			@QueryParam("Username") String username, 
			@QueryParam("Password") String password) throws SystemException, NumberFormatException, PortalException {
		log.info("Username: " + username + ", password: " + password);
		
		JSONObject jSonData = new JSONFactoryUtil().createJSONObject();
		
		if( checkValidParam(username, password)){
			// Check
			if( AuthUtil.hasAccess(username, password)){
				jSonData = JsonUtil.DanhSachCQQLHuyenXa();
				
			} else{
				jSonData.put("ErrorCode","2");
				jSonData.put("Message", "Truy cập không hợp lệ. Kiểm tra lại tên đăng nhập và mật khẩu");
			}			
		}else{
			jSonData.put("ErrorCode","1");
			jSonData.put("Message", "Tham số không đủ hoặc không hợp lệ");
		}
		return jSonData.toString();
	}
	
	
	@GET
	@Path("v1/TraCuuHoSo")
	@Produces("application/json")
	@Consumes("application/json")
	public String TraCuuHoSo(
			@QueryParam("Username") String username, 
			@QueryParam("Password") String password,
			@QueryParam("MaBienNhan") String mabn) throws SystemException, NumberFormatException, PortalException {
		log.info("Username: " + username + ", password: " + password + " mahs: " + mabn);
		
		JSONObject jSonData = new JSONFactoryUtil().createJSONObject();
		
		if( checkValidParam(username, password)){
			// Check
			if( AuthUtil.hasAccess(username, password)){
				jSonData = JsonUtil.TraCuuHoSo(mabn);
				
			} else{
				jSonData.put("ErrorCode","2");
				jSonData.put("Message", "Truy cập không hợp lệ. Kiểm tra lại tên đăng nhập và mật khẩu");
			}			
		}else{
			jSonData.put("ErrorCode","1");
			jSonData.put("Message", "Tham số không đủ hoặc không hợp lệ");
		}
		return jSonData.toString();
	}
}
