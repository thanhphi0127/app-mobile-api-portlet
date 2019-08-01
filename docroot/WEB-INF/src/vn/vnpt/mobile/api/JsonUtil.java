/**
 * Author: PhiNT
 * Create date: 01/07/2019
 */
package vn.vnpt.mobile.api;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import vn.dtt.cmon.dao.hosohcc.model.HoSoTTHCCong;
import vn.dtt.cmon.dao.hosohcc.service.HoSoTTHCCongLocalServiceUtil;
import vn.vnpt.cmon.dao.core.model.ThongKeObject;
import vn.vnpt.cmon.dao.core.model.ThongKeTrongKy;
import vn.vnpt.cmon.dao.core.service.ThongKeObjectLocalServiceUtil;
import vn.vnpt.cmon.dao.core.service.ThongKeTrongKyLocalServiceUtil;
import vn.vnpt.dao.cqql.model.CoQuanQuanLy;
import vn.vnpt.dao.cqql.model.CoQuanQuanLy2LinhVuc;
import vn.vnpt.dao.cqql.service.CoQuanQuanLy2LinhVucLocalServiceUtil;
import vn.vnpt.dao.cqql.service.CoQuanQuanLyLocalServiceUtil;
import vn.vnpt.dao.hosohcc.service.TrangThaiHoSoLocalServiceUtil;

import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;

public class JsonUtil {
	
	static final Integer begin = 0;
	static final Integer end = 1;
	
	/**
	 * Thống kê hồ sơ tổng hợp (năm hiện tại)
	 * @return
	 */
	public static JSONObject ThongKeTongHop(){
		
		// Current year
		String thisYear = new SimpleDateFormat("yyyy").format(new Date());
		String tuNgay ="01/01/" + thisYear;
		String denNgay ="12/31/" + thisYear;
		
		// Get data
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int tongHoSoDaTiepNhan = ThongKeObjectLocalServiceUtil.countForHoSoDaTiepNhan(tuNgay, denNgay,0,0); 
		int tongHoSoDungHan = ThongKeObjectLocalServiceUtil.countForHoSoDungHan(tuNgay, denNgay,0,0); 
		int tongHoSoQuaHan = ThongKeObjectLocalServiceUtil.countForHoSoQuaHan(tuNgay, denNgay,0,0); 
		int tongHoSoDaXuLy = tongHoSoDungHan + tongHoSoQuaHan;
		int tongHoSoTrucTuyen = ThongKeObjectLocalServiceUtil.countForHoSoTrucTuyen(tuNgay, denNgay, 0, 0);
		double tongDungQua = ((double)tongHoSoQuaHan)/(tongHoSoDaTiepNhan);
		int dangXuLy = tongHoSoDaTiepNhan - tongHoSoDaXuLy;
		
		JSONObject data = new JSONFactoryUtil().createJSONObject();
		data.put("TongHS", tongHoSoDaTiepNhan);
		data.put("DaXuLy", tongHoSoDaXuLy);
		data.put("QuaHan", tongHoSoQuaHan);
		data.put("DangXuLy", dangXuLy);
		
		return data;
	}
	
	/**
	 * Thống kế tình hình xử lý TTHC theo Quận / Huyện (năm hiện tại)
	 * @return
	 */
	public static JSONObject ThongKeQuanHuyen(){
		JSONObject data = new JSONFactoryUtil().createJSONObject();
		ThongKeObject hs;
		String thisYear = new SimpleDateFormat("yyyy").format(new Date());
		try {
			ThongKeTrongKy tk = ThongKeTrongKyLocalServiceUtil.getDuLieuThongKe(Long.valueOf(thisYear), 3, QueryUtil.ALL_POS,QueryUtil.ALL_POS).get(0);
			
			data.put("TruocHan", tk.getSom());
			data.put("DungHan", tk.getDung());
			data.put("TreHan", tk.getTre());
			data.put("DangXuLy", tk.getChuaDenHan());
			data.put("QuaHan", tk.getQuaHan());
			data.put("ChoRut", tk.getRutHoSo());
			data.put("DungXuLy", tk.getKhongHopLe());
			data.put("TongHS", tk.getTong());
			
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return data;
	}
	
	/**
	 * Thống kế tình hình xử lý TTHC theo Sở / Ban / Ngành (năm hiện tại)
	 * @return
	 */
	public static JSONObject ThongKeSoBanNganh(){
		JSONObject data = new JSONFactoryUtil().createJSONObject();
		ThongKeObject hs;
		String thisYear = new SimpleDateFormat("yyyy").format(new Date());
		try {
			ThongKeTrongKy tk = ThongKeTrongKyLocalServiceUtil.getDuLieuThongKe(Long.valueOf(thisYear), 5, QueryUtil.ALL_POS,QueryUtil.ALL_POS).get(0);
			
			data.put("TruocHan", tk.getSom());
			data.put("DungHan", tk.getDung());
			data.put("TreHan", tk.getTre());
			data.put("DangXuLy", tk.getChuaDenHan());
			data.put("QuaHan", tk.getQuaHan());
			data.put("ChoRut", tk.getRutHoSo());
			data.put("DungXuLy", tk.getKhongHopLe());
			data.put("TongHS", tk.getTong());
			
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return data;
	}
	
	/**
	 * Thống kê Tình hình xử lý hồ sơ theo đơn vi
	 * @param tungay String { MM/dd/yyyy }
	 * @param denngay String { MM/dd/yyyy }
	 * @param CoQuanId Integer
	 * @return
	 */
	public static JSONObject ThongKeTinhHinhXLHS(Integer CoQuanId, String tungay, String denngay){
		JSONObject data = new JSONFactoryUtil().createJSONObject();
		try {
			int tongHoSoDaTiepNhan = ThongKeObjectLocalServiceUtil.countForHoSoDaTiepNhan(tungay, denngay, CoQuanId, 0); 
			int tongHoSoDungHan = ThongKeObjectLocalServiceUtil.countForHoSoDungHan(tungay, denngay, CoQuanId, 0); 
			int tongHoSoQuaHan = ThongKeObjectLocalServiceUtil.countForHoSoQuaHan(tungay, denngay, CoQuanId, 0); 
			int tongHoSoDaXuLy = tongHoSoDungHan + tongHoSoQuaHan;
			double tongDungQua = ((double)tongHoSoQuaHan)/(tongHoSoDaTiepNhan);
			int dangXuLy = tongHoSoDaTiepNhan - tongHoSoDaXuLy;
			
			data.put("TongHS", tongHoSoDaTiepNhan);
			data.put("DaTiepNhan", tongHoSoDaTiepNhan);
			data.put("DangXuLy", dangXuLy);
			data.put("DungHan", tongHoSoDungHan);
			data.put("QuaHan", tongHoSoQuaHan);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return data;
	}
	
	/**
	 * Thống kê TTHC theo xã phường (năm hiện tại)
	 * @return
	 */
	public static JSONObject ThongKeXaPhuong(String... years){
		JSONObject data = new JSONFactoryUtil().createJSONObject();
		String thisYear = new SimpleDateFormat("yyyy").format(new Date());
		
		if(years.length == 1 && years[0].length() == 4){
			thisYear = years[0];
		}
		
		try {
			ThongKeTrongKy tk = ThongKeTrongKyLocalServiceUtil.getDuLieuThongKe(Long.valueOf(thisYear), 4, QueryUtil.ALL_POS,QueryUtil.ALL_POS).get(0);
			
			data.put("TruocHan", tk.getSom());
			data.put("DungHan", tk.getDung());
			data.put("TreHan", tk.getTre());
			data.put("DangXuLy", tk.getChuaDenHan());
			data.put("QuaHan", tk.getQuaHan());
			data.put("ChoRut", tk.getRutHoSo());
			data.put("DungXuLy", tk.getKhongHopLe());
			data.put("TongHS", tk.getTong());
			
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return data;
	}
	
	/**
	 * Thống kê theo lĩnh vực TTHC
	 * @param coQuanId Integer
	 * @param tungay String { MM/dd/yyyy }
	 * @param denngay String { MM/dd/yyyy }
	 * @return
	 */
	private static JSONObject ThongKeLinhVucHC(Integer coQuanId, String tungay, String denngay){
		JSONObject data = new JSONFactoryUtil().createJSONObject();
		if( coQuanId == null) coQuanId = 81620;
		if( tungay == null || tungay.equals("") || denngay == null || denngay.equals("")){
			data.put("ErrorCode", 1);
			data.put("Message", "Tham số truyền vào không đúng định dạng");
		}
		
		try {
			JSONArray arrLinhVuc = JSONFactoryUtil.createJSONArray();
			List<CoQuanQuanLy2LinhVuc> getDSLinhVucByCoQuan = CoQuanQuanLy2LinhVucLocalServiceUtil.getAllLinhVucByCoQuanQuanLyID(coQuanId);
			for(CoQuanQuanLy2LinhVuc cqql2lv : getDSLinhVucByCoQuan ){
				
				int tongTiepNhanLinhVuc =ThongKeObjectLocalServiceUtil.countForHoSoDaTiepNhan(tungay, denngay,coQuanId,cqql2lv.getNHOMTHUTUCHANHCHINHID());
				int tongHoSoTrucTuyenLinhVuc = ThongKeObjectLocalServiceUtil.countForHoSoTrucTuyen(tungay, denngay,coQuanId,cqql2lv.getNHOMTHUTUCHANHCHINHID());
				int tongDungHanLinhVuc = ThongKeObjectLocalServiceUtil.countForHoSoDungHan(tungay, denngay,coQuanId,cqql2lv.getNHOMTHUTUCHANHCHINHID());
				int tongQuaHanLinhVuc = ThongKeObjectLocalServiceUtil.countForHoSoQuaHan(tungay, denngay,coQuanId,cqql2lv.getNHOMTHUTUCHANHCHINHID());
				int tongDangXulyLinhVuc = (ThongKeObjectLocalServiceUtil.countForHoSoDaTiepNhan(tungay, denngay,coQuanId,cqql2lv.getNHOMTHUTUCHANHCHINHID())-
				          (tongDungHanLinhVuc + tongQuaHanLinhVuc));
				double tiLeDungHanLinhVuc = ((double)tongDungHanLinhVuc)/(tongDungHanLinhVuc + tongQuaHanLinhVuc);
				
				JSONObject jsonLV = new JSONFactoryUtil().createJSONObject();
				jsonLV.put("TenLV", cqql2lv.getTENNHOMTTHC());
				jsonLV.put("TongHS", tongTiepNhanLinhVuc);
				jsonLV.put("DungHan", tongDungHanLinhVuc);
				jsonLV.put("DangXuLy", tongDangXulyLinhVuc);
				jsonLV.put("QuaHan", tongQuaHanLinhVuc);
				
				// System.out.println(jsonLV.toString() + cqql2lv.getNHOMTHUTUCHANHCHINHID());
				
				arrLinhVuc.put(jsonLV);
			}
			
			data.put("List", arrLinhVuc);
			data.put("Message", "Danh sách thống kê hồ sơ theo lĩnh vực");
			data.put("ErrorCode", 0);
			
		} catch (Exception e){
			data.put("ErrorCode", 2);
			data.put("Message", "Lỗi lấy dữ liệu từ server");
		}
		return data;
	}
	
	/**
	 * Thống kê xử lý hồ sơ theo lĩnh vực + theo tháng
	 * @param coQuanId
	 * @return
	 */
	public static JSONObject ThongKeTheoThang(Integer coQuanId){
		String[] month = getDayOfMonth();
		JSONObject rs = ThongKeLinhVucHC(coQuanId, month[begin], month[end]);
		rs.put("Data", ThongKeTinhHinhXLHS(coQuanId, month[begin], month[end]));
		return rs;
	}
	
	/**
	 * Thống kê xử lý hồ sơ theo lĩnh vực + theo tuần
	 * @param coQuanId
	 * @return
	 */
	public static JSONObject ThongKeTheoTuan(Integer coQuanId){
		String[] week = getDayOfWeek();
		JSONObject rs = ThongKeLinhVucHC(coQuanId, week[begin], week[end]);
		rs.put("Data", ThongKeTinhHinhXLHS(coQuanId, week[begin], week[end]));
		return rs;
	}

	
	/**
	 * Hàm thống kê xử lý hồ sơ theo lĩnh vực + hôm nay
	 * @param coQuanId
	 * @return
	 */
	public static JSONObject ThongKeHomNay(Integer coQuanId){
		JSONObject rs = ThongKeLinhVucHC(coQuanId, currentDate(), currentDate());
		rs.put("Data", ThongKeTinhHinhXLHS(coQuanId, currentDate(), currentDate()));
		return rs;
	}
	
	/**
	 * Lấy danh sách cơ quan quản lý theo cấp: Ex: 5 Cấp Sở/Ban/Ngành, 4: Cấp huyên, 3: Cấp xã
	 * @param type Integer
	 * @return
	 */
	public static JSONObject DanhSachCQQL(Integer type){
		if(type == null) type = 5;
		JSONObject data = new JSONFactoryUtil().createJSONObject();
		try {
			JSONArray arrCoQuan = JSONFactoryUtil.createJSONArray();
			List<CoQuanQuanLy> listCQQL = CoQuanQuanLyLocalServiceUtil.getCoQuanQuanLyByCap(type);
			
			for (CoQuanQuanLy coQuanQuanLy : listCQQL) {
				JSONObject jsonLV = new JSONFactoryUtil().createJSONObject();
				jsonLV.put("IdCQ", coQuanQuanLy.getId());
				jsonLV.put("TenCQ", coQuanQuanLy.getTen());
				arrCoQuan.put(jsonLV);
			}
			
			data.put("Data", arrCoQuan);
			data.put("Message", "Danh sách cơ quan quản lý");
			data.put("ErrorCode", 0);
		} catch (SystemException e) {
			data.put("ErrorCode", 2);
			data.put("Message", "Lỗi lấy dữ liệu từ server");
		}
		return data;
		
	}
	
	/**
	 * Lấy danh sách cơ quan quản lý theo cấp huyên (xã)
	 * @return
	 */
	public static JSONObject DanhSachCQQLHuyenXa(){
		JSONObject data = new JSONFactoryUtil().createJSONObject();
		try {
			
			JSONArray arrCoQuan = JSONFactoryUtil.createJSONArray();
			List<CoQuanQuanLy> listCQQL = CoQuanQuanLyLocalServiceUtil.getCoQuanQuanLyByCap(4);
			
			for (CoQuanQuanLy coQuanQuanLy : listCQQL) {
				JSONObject jsonLV = new JSONFactoryUtil().createJSONObject();
				jsonLV.put("IdCQ", coQuanQuanLy.getId());
				jsonLV.put("TenCQ", coQuanQuanLy.getTen());
				
				arrCoQuan.put(jsonLV);
				jsonLV.put("Chilren", DanhSachCQQL(3).getJSONArray("Data"));
			}
			
			data.put("Data", arrCoQuan);
			data.put("Message", "Danh sách cơ quan quản lý");
			data.put("ErrorCode", 0);
			
		} catch (SystemException e) {
			data.put("ErrorCode", 2);
			data.put("Message", "Lỗi lấy dữ liệu từ server");
		}
		
		return data;
	}
	
	/**
	 * Tra cứu hồ sơ theo mã số biên nhận
	 * @param MaSoBienNhan
	 * @return
	 */
	public static JSONObject TraCuuHoSo(String MaSoBienNhan){
		JSONObject data = new JSONFactoryUtil().createJSONObject();
		
		if(MaSoBienNhan == null || MaSoBienNhan.equals("")){
			data.put("ErrorCode", 3);
			data.put("Message", "Thiếu tham số mã biên nhận");
		}
		
		try {
			// System.out.println("PHINT ===== begin");
			JSONArray arrList = JSONFactoryUtil.createJSONArray();
			List<HoSoTTHCCong> listResultHoSo = new ArrayList<HoSoTTHCCong>();
			listResultHoSo = HoSoTTHCCongLocalServiceUtil.findHoSoByMaSoBienNhanOrSoCMNDNguoiNop(MaSoBienNhan, "");
		    
			int i = 0;
			// System.out.println("PHINT ===== soluong HS" + listResultHoSo.size());
			
			for (HoSoTTHCCong hoSoTTHCCong : listResultHoSo) {
				
				JSONObject hs = new JSONFactoryUtil().createJSONObject();
				hs.put("MaSoBienNhan", hoSoTTHCCong.getMaSoBienNhan());
				hs.put("TenTHHC", hoSoTTHCCong.getTenThuTucHanhChinh());
				hs.put("NgayNop", hoSoTTHCCong.getNgayNopHoSo());
				hs.put("NgayhenTra", hoSoTTHCCong.getNgayHenTraKetQua());
				hs.put("NgayTraHS", hoSoTTHCCong.getNgayTraKetQua());
				hs.put("NguoiNop", hoSoTTHCCong.getMaSoBienNhan());
				
				hs.put("CMND", hoSoTTHCCong.getCmndChuHoSo());
				// hs.put("TrangThaiHS", hoSoTTHCCong.getTrangThaiHoSo() > 0L? TrangThaiHoSoLocalServiceUtil.fetchTrangThaiHoSo(hoSoTTHCCong.getTrangThaiHoSo()).getTrangThai() : "");
				hs.put("NguoiNop", hoSoTTHCCong.getHoTenNguoiNopHoSo());
				hs.put("LoaiHS", hoSoTTHCCong.getLoaiHoSo() == 1 ? "Trực tiếp" : "Trực tuyến");
				hs.put("CoQuanTiepNhan", hoSoTTHCCong.getTenCoQuanTiepNhan());
				hs.put("MaSoBienNhan", hoSoTTHCCong.getMaSoBienNhan());
				arrList.put(hs);
				
				if(i++ > 100) break;
				
			}
			
			data.put("Data", arrList);
			data.put("ListSize", listResultHoSo.size());
			data.put("Message", "Danh sách hồ sơ theo mã biên nhận");
			data.put("ErrorCode", 0);
			
		} catch (SystemException e) {
			System.out.println("ERROR: PHINT ===== " + e);
			data.put("ErrorCode", 2);
			data.put("Message", "Lỗi lấy dữ liệu từ server");
		}
		
		return data;
	}
	
		
	/**
	 * Xác định ngày bắt đầu, ngày kết thúc trong tuần (theo ngày hiện tại)
	 * @return new String[2] { beginDate, endDate }, Format { MM/dd/yyyy }
	 */
	public static String [] getDayOfWeek(){
        Calendar c = GregorianCalendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        
        String startDate = "", endDate = "";
        startDate = df.format(c.getTime());
        c.add(Calendar.DATE, 6);
        endDate = df.format(c.getTime());
        
        return new String[] {startDate, endDate};
	}
	
	/**
	 * Lấy ngày hiện tại hệ thống
	 * @return String { MM/dd/yyyy }
	 */
	public static String currentDate(){
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date date = new Date();
		return dateFormat.format(date);
	}
	
	/**
	 * Xác định ngày bắt đầu, ngày kết thúc trong tháng (theo ngày hiện tại)
	 * @return new String[2] { beginDate, endDate }, Format { MM/dd/yyyy }
	 */
	public static String[] getDayOfMonth(){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        
        String startDate = "", endDate = "";
        startDate = df.format(c.getTime());
        c.add(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH) - 1);
        endDate = df.format(c.getTime());
        
        return new String[] {startDate, endDate};
	}
}
