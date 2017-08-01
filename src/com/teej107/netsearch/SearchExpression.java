package com.teej107.netsearch;

/**
 * Created by teej107 on 7/31/2017.
 */
public class SearchExpression
{
	private String format;
	private int args;

	public SearchExpression(String format, int args)
	{
		this.format = format;
		this.args = args;
	}

	public String format(Object... args)
	{
		if(args.length != this.args)
			return null;

		return String.format(format, args);
	}
}
