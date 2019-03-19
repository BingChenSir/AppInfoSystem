package cn.appsys.tools;

/**
 * 分页工具类
 * @author Administrator
 *
 */
public class PageSupport {
	
	/**
	 * 当前页码-来自于用户输入
	 */
	private int currentPageNo = 1;
	
	/**
	 * 总数量(表)
	 */
	private int totalCount = 0;
	
	/**
	 * 总页容量
	 */
	private int pageSize = 5;
	
	/**
	 * 总页数-totalCount/pageSize(+1)
	 */
	private int totalPageCount = 1;

	public int getCurrentPageNo() {
		return currentPageNo;
	}

	public void setCurrentPageNo(int currentPageNo) {
		if(currentPageNo > 0){
			this.currentPageNo = currentPageNo;
		}
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		if(totalCount > 0){
			this.totalCount = totalCount;
			//设置总页数
			if(this.totalCount % this.pageSize == 0){
				this.totalPageCount = this.totalCount / this.pageSize;
			}else if(this.totalCount % this.pageSize > 0){
				this.totalPageCount = this.totalCount / this.pageSize + 1;
			}else{
				this.totalPageCount = 0;
			}
		}
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalPageCount() {
		return totalPageCount;
	}

	public void setTotalPageCount(int totalPageCount) {
		this.totalPageCount = totalPageCount;
	}
	
	
}
