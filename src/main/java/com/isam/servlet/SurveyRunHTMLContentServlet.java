package com.isam.servlet;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.isam.console.Survey;
import com.isam.console.SurveyToHtml;
import com.isam.console.SurveyTopicXHtml;
import com.isam.console.SurveyXHtml;
import com.isam.helper.DataUtil;

public class SurveyRunHTMLContentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SurveyToHtml dao = new SurveyToHtml();
		Map<String,String> inds = dao.getSurveyIndustry();
		Map<String,String> regions = dao.getSurveyRegion();
		List<List<String>> qTypeYearList =dao.getQTypeAndYear();
		Map<String,Map<String,String>> abroad = dao.getAbroadValue();
		long starttime,endtime,time;
		starttime=System.currentTimeMillis();
		System.out.println(starttime);
		for (List<String> list:qTypeYearList) {
			String qType= list.get(0);
			String year=list.get(1);
			String lastyear=String.valueOf(Integer.valueOf(year)-1);
			String nextyear=String.valueOf(Integer.valueOf(year)+1);
			List<String> qNoList= dao.getqNoByQTypeAndYear(qType,year);
			List<SurveyTopicXHtml> htmlTemplates=dao.getSurveyTopicXHtml(qType,year);
			Map<String,List<String>> map = new HashMap<String, List<String>>();
			Map<String,String> topicXhtmlmap = new HashMap<String, String>();
			List<SurveyXHtml> result = new ArrayList<SurveyXHtml>();
			Pattern ptn2 = Pattern.compile("@[a-z0-9]{2,3}_[a-z0-9]{1,4}[_[-|a-zA-Z0-9]{1,4}]*<");
			for(int j=0;j<htmlTemplates.size();j++){
				SurveyTopicXHtml topicXHtml=htmlTemplates.get(j);
				String html = topicXHtml.getHtml().trim();
				String topic = topicXHtml.getTopic();
				Matcher match2 = ptn2.matcher(html);
				List<String> items = new ArrayList<String>();
				while(match2.find()){
					String text =html.substring(match2.start(),match2.end()-1);
					items.add(text);
//					System.out.println(text);
				}
				map.put(topic, items);
				html = topicXHtml.getHtml().replaceAll("@year", year).replaceAll("@lastyear", lastyear).replaceAll("@nextyear", nextyear);
				topicXhtmlmap.put(topic, html);
			}

			Map<String,String> a10PLmap = new HashMap<String, String>();
			for (int i = 0; i < qNoList.size(); i++) {
				time=System.currentTimeMillis();
				for(Entry<String, String> m :topicXhtmlmap.entrySet()){
					String topic = m.getKey();
					String html = m.getValue();
					String qNo=qNoList.get(i);
					List<String> tempList = map.get(topic);
					List<String> items = new ArrayList<String>();
					items.addAll(tempList);
					int tempCount=items.size();
					List<Survey> surveys=dao.getSurveyValue(qType,year,qNo,topic);
					for(int k=0;k<surveys.size();k++){
						Survey survey =surveys.get(k);
						String value = survey.getValue()==null?"&nbsp;":survey.getValue();
						String item = "@"+survey.getItem();
						String type=survey.getType();

						if(qType.equalsIgnoreCase("Abroad")){
							if(type.equals("difficulty")||type.equals("motive")||type.equals("state")){
								Map<String,String> typeMap = abroad.get(type);
								if(survey.getItem().startsWith("a10_a")||survey.getItem().startsWith("a10_b")){
									int temp = -1;
									if(value!=""&&DataUtil.isMatchPtn(value, "\\d*")){
										temp=Integer.valueOf(value);
									}
									String keyname=item+"_L";
									if(temp!=-1&&temp>10){
										keyname=item+"_L";
									}else if(temp>0&&temp<=10) {
										keyname=item+"_P";
									}
									if(typeMap.containsKey(value)){
										a10PLmap.put(keyname, typeMap.get(value)==null?"&nbsp;":typeMap.get(value));
									}
								}else{
									if(typeMap.containsKey(value)){
										value=typeMap.get(value);
									}
								}
							}
						}
						
						
						if(type.equals("Industry")){
							int lenValue=value.length();
							if(lenValue==2&&DataUtil.isMatchPtn(value, "\\d{2}")){
								value=inds.get(value);
							}else if(DataUtil.isMatchPtn(value, "\\d{2,}+")&&lenValue%2==0){
								StringBuffer sb = new StringBuffer();
								for(int l=0;l<lenValue/2;l++){
									if(l!=(lenValue/2)-1){
										sb.append(inds.get(value.substring(2*l, 2*l+1)));
										sb.append("、");
									}
								}
								value=sb.toString();
								sb.setLength(0);
							}
						}
						if(type.equals("distraction")){
							int lenValue=value.length();
							if(lenValue==2&&DataUtil.isMatchPtn(value, "\\d{2}")){
								value=regions.get(value);
							}else if(DataUtil.isMatchPtn(value, "\\d{2,}+")&&lenValue%2==0){
								StringBuffer sb = new StringBuffer();
								for(int l=0;l<lenValue/2;l++){
									sb.append(regions.get(value.substring(2*l, 2*l+1)));
									if(l!=(lenValue/2)-1){
										sb.append("、");
									}
								}
								value=sb.toString();
								sb.setLength(0);
							}
						}
						if(type.equals("square")||type.equals("USD")||type.equals("NTD")||type.equals("person")||type.equals("number")){
							if(DataUtil.isMatchPtn(value, "\\d*")){
								DecimalFormat fmt = new DecimalFormat("#,###");
								value = fmt.format(Double.valueOf(value));
							}
						}
						if(items.contains(item)){
							html = html.replace(item+"<", value+"<");
							items.remove(item);
						}else{
							String temp = item+"_"+value;
							if(items.contains(temp)){
								html = html.replace(temp+"<", "1<");
								items.remove(item);
							}
						}
					}//end of surveys
//					System.out.println(items.size()+";tempCount:"+tempCount);
					for(Entry<String, String>  a : a10PLmap.entrySet()){
						html = html.replace(a.getKey()+"<", a.getValue()+"<");
						items.remove(a.getKey());
					}
					for(String s:items){
						if(s.indexOf("_")==s.lastIndexOf("_")){
//							System.out.println("s <:"+s);
							html = html.replace(s+"<", "&nbsp;<");
						}else{
//							System.out.println("s0<:"+s);
							html = html.replace(s+"<", "0<");
						}
					}
//					System.out.println(html);
					SurveyXHtml bean= new SurveyXHtml();
					bean.setHtml(html);
					bean.setqNo(qNo);
					bean.setqType(qType);
					bean.setTopic(topic);
					bean.setYear(year);
					result.add(bean);
					if(result.size()>=1500){
						dao.insertBatch(result);
						result.clear();
					}
				}
				endtime=System.currentTimeMillis();
//				System.out.println("until colse spend:"+(endtime-time)/1000+"秒");
			}
			if(result.size()>0){
				dao.insertBatch(result);
				result.clear();
			}
			System.out.println("end");
			endtime=System.currentTimeMillis();
			System.out.println("until colse spend:"+(endtime-starttime)/1000+"秒");
		}

	}
}
