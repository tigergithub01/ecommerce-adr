package com.ecommerce.utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class PagerFragment {
	private int id;
	private Fragment fragment;
	private Bundle parameters;
	private String pageTitle;

	public PagerFragment() {

	}

	public PagerFragment(int id, Fragment fragment) {
		this.id = id;
		this.fragment = fragment;
	}
	
	
	public PagerFragment(int id, Fragment fragment,Bundle parameters) {
		this.id = id;
		this.fragment = fragment;
		this.parameters = parameters;
	}
	
	public PagerFragment(Fragment fragment,String pageTitle) {
		this.fragment = fragment;
		this.pageTitle = pageTitle;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Fragment getFragment() {
		return fragment;
	}

	public void setFragment(Fragment fragment) {
		this.fragment = fragment;
	}

	public Bundle getParameters() {
		return parameters;
	}

	public void setParameters(Bundle parameters) {
		this.parameters = parameters;
	}

	public String getPageTitle() {
		return pageTitle;
	}

	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}
	
	
}
