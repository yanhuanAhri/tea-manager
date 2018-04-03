package com.tea.mservice.core.util;

public class PageBean {

	private int currentPage = 1;// 当前页数

	public int totalPages = 0;// 总页数

	private int pageSize = 0;// 每页显示数

	private int totalRows = 0;// 总数据数

	private int startNum = 0;// 开始记录

	private int nextPage = 0;// 下一页

	private int previousPage = 0;// 上一页

	private boolean hasNextPage = false;// 是否有下一页

	private boolean hasPreviousPage = false;// 是否有前一页

	public PageBean(int pageSize, int currentPage, int totalRows) {

		this.pageSize = pageSize;
		this.currentPage = currentPage;
		this.totalRows = totalRows;

		if ((totalRows % pageSize) == 0) {
			this.totalPages = totalRows / pageSize;
		} else {
			this.totalPages = totalRows / pageSize + 1;
		}

		if (currentPage >= this.totalPages) {
			this.hasNextPage = false;
			currentPage = this.totalPages;
		} else {
			this.hasNextPage = true;
		}

		if (currentPage <= 1) {
			this.hasPreviousPage = false;
			currentPage = 1;
		} else {
			this.hasPreviousPage = true;
		}

		this.startNum = (currentPage - 1) * pageSize;

		this.nextPage = currentPage + 1;

		if (this.nextPage >= this.totalPages) {
			this.nextPage = this.totalPages;
		}

		this.previousPage = currentPage - 1;

		if (this.previousPage <= 1) {
			this.previousPage = 1;
		}

	}

	public boolean isHasNextPage() {

		return this.hasNextPage;

	}

	public boolean isHasPreviousPage() {

		return this.hasPreviousPage;

	}

	/**
	 * @return the nextPage
	 */
	public int getNextPage() {
		return this.nextPage;
	}

	/**
	 * @param nextPage
	 *            the nextPage to set
	 */
	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}

	/**
	 * @return the previousPage
	 */
	public int getPreviousPage() {
		return previousPage;
	}

	/**
	 * @param previousPage
	 *            the previousPage to set
	 */
	public void setPreviousPage(int previousPage) {
		this.previousPage = previousPage;
	}

	/**
	 * @return the currentPage
	 */
	public int getCurrentPage() {
		return this.currentPage;
	}

	/**
	 * @param currentPage
	 *            the currentPage to set
	 */
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return this.pageSize;
	}

	/**
	 * @param pageSize
	 *            the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @return the totalPages
	 */
	public int getTotalPages() {
		return this.totalPages;
	}

	/**
	 * @param totalPages
	 *            the totalPages to set
	 */
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	/**
	 * @return the totalRows
	 */
	public int getTotalRows() {
		return this.totalRows;
	}

	/**
	 * @param totalRows
	 *            the totalRows to set
	 */
	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}

	/**
	 * @param hasNextPage
	 *            the hasNextPage to set
	 */
	public void setHasNextPage(boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}

	/**
	 * @param hasPreviousPage
	 *            the hasPreviousPage to set
	 */
	public void setHasPreviousPage(boolean hasPreviousPage) {
		this.hasPreviousPage = hasPreviousPage;
	}

	/**
	 * @return the startNum
	 */
	public int getStartNum() {
		return startNum;
	}

	/**
	 * @param startNum
	 *            the startNum to set
	 */
	public void setStartNum(int startNum) {
		this.startNum = startNum;
	}
}