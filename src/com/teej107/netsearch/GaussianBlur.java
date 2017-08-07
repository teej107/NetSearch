package com.teej107.netsearch;

import java.awt.*;
import java.awt.image.*;

/**
 * Created by teej107 on 8/2/2017.
 */
public class GaussianBlur
{
	private ConvolveOp horizontal, vertical;
	private int radius;
	private float sigma;
	private float[] data;
	private Color edgeColor;

	public GaussianBlur(int radius, float sigma, Color edgeColor)
	{
		this.radius = radius;
		this.sigma = sigma;
		this.edgeColor = edgeColor;
	}

	public int getRadius()
	{
		return radius;
	}

	public void setRadius(int radius)
	{
		this.radius = radius;
		this.data = null;
	}

	public float getSigma()
	{
		return sigma;
	}

	public void setSigma(float sigma)
	{
		this.sigma = sigma;
		this.data = null;
	}

	public Color getEdgeColor()
	{
		return edgeColor;
	}

	public void setEdgeColor(Color edgeColor)
	{
		this.edgeColor = edgeColor;
	}

	public BufferedImage blur(BufferedImage image)
	{
		if (data == null)
		{
			this.data = getGaussianBlurMatrix(radius, sigma);
			this.horizontal = new ConvolveOp(new Kernel(data.length, 1, data), ConvolveOp.EDGE_NO_OP, null);
			this.vertical = new ConvolveOp(new Kernel(1, data.length, data), ConvolveOp.EDGE_NO_OP, null);
		}

		int pad = radius * 2;
		BufferedImage bi = new BufferedImage(image.getWidth() + pad, image.getHeight() + pad, BufferedImage.TYPE_INT_RGB);
		Graphics g = bi.createGraphics();
		g.setColor(edgeColor);
		g.fillRect(0, 0, bi.getWidth(), bi.getHeight());
		g.drawImage(image, radius, radius, null);
		g.dispose();

		bi = horizontal.filter(vertical.filter(bi, null), null);
		return bi.getSubimage(radius, radius, image.getWidth(), image.getHeight());
	}

	private static float[] getGaussianBlurMatrix(int radius, float sigma)
	{
		if (radius < 1)
		{
			throw new IllegalArgumentException("Radius must be >= 1");
		}

		int size = radius * 2 + 1;
		float[] data = new float[size];

		float twoSigmaSquare = 2.0f * sigma * sigma;
		float sigmaRoot = (float) Math.sqrt(twoSigmaSquare * Math.PI);
		float total = 0.0f;

		for (int i = -radius; i <= radius; i++)
		{
			float distance = i * i;
			int index = i + radius;
			data[index] = (float) Math.exp(-distance / twoSigmaSquare) / sigmaRoot;
			total += data[index];
		}

		for (int i = 0; i < data.length; i++)
		{
			data[i] /= total;
		}
		return data;
	}
}
