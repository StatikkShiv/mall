package com.taotao.common.bean;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EasyUIResult {

	// 定义jackson对象
	// 实现序列化的用法String teststringlist=objectMapper.writeValueAsString(list);
	private static final ObjectMapper MAPPER = new ObjectMapper();
	// 总记录数
	private Integer total;

	private List<?> rows;

	public EasyUIResult() {
	}

	public EasyUIResult(Integer total, List<?> rows) {
		this.total = total;
		this.rows = rows;
	}

	public EasyUIResult(Long total, List<?> rows) {
		// 把Integer类型转化为Int类型
		this.total = total.intValue();
		this.rows = rows;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public List<?> getRows() {
		return rows;
	}

	public void setRows(List<?> rows) {
		this.rows = rows;
	}

	/**
	 * Jackson处理一般的JavaBean和Json之间的转换只要使用ObjectMapper
	 * 对象的readValue和writeValueAsString两个方法就能实现。但是如果要转换复杂类型Collection如
	 * List<YourBean>，那么就需要先反序列化复杂类型 为泛型的Collection Type。
	 * 
	 * 如果是ArrayList<YourBean>那么使用ObjectMapper
	 * 的getTypeFactory().constructParametricType(collectionClass,
	 * elementClasses);
	 * 
	 * 如果是HashMap<String,YourBean>那么 ObjectMapper
	 * 的getTypeFactory().constructParametricType(HashMap.class,String.class,
	 * YourBean.class);
	 */
	public static EasyUIResult formatToList(String jsonData, Class<?> clazz) {
		try {
			// 将Json串以树状结构读入内存
			JsonNode jsonNode = MAPPER.readTree(jsonData);
			// 得到results这个节点下的信息
			JsonNode data = jsonNode.get("rows");
			List<?> list = null;
			if (data.isArray() && data.size() > 0) {
				list = MAPPER.readValue(data.traverse(),
						MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
			}
			return new EasyUIResult(jsonNode.get("total").intValue(), list);
		} catch (Exception e) {
			return null;
		}
	}

}
