package com.company.assetmanagment.exception;

@SuppressWarnings("serial")
public class ResourceAlreadyExists extends RuntimeException
{

	public ResourceAlreadyExists(String message)
	{
		super(message);
	}
}