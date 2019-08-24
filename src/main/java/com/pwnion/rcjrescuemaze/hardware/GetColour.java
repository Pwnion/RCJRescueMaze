package com.pwnion.rcjrescuemaze.hardware;

import java.io.IOException;
import java.util.HashMap;

import com.google.inject.Inject;

public class GetColour extends Camera {
	int avgColours[];
	
	HashMap<String, HashMap<String, Boolean>> colourRanges = new HashMap<String, HashMap<String, Boolean>>()
	{
		private static final long serialVersionUID = 1L;
		{
			put("Silver", new HashMap<String, Boolean>() {
				private static final long serialVersionUID = 1L;
				{
					put("Red", avgColours[0] > 0 && avgColours[0] < 255 ? true : false);
					put("Green", avgColours[1] > 0 && avgColours[1] < 255 ? true : false);
					put("Blue", avgColours[2] > 0 && avgColours[2] < 255 ? true : false);
				}
			});
		}
		{
			put("Black", new HashMap<String, Boolean>() {
				private static final long serialVersionUID = 1L;
				{
					put("Red", avgColours[0] > 0 && avgColours[0] < 255 ? true : false);
					put("Green", avgColours[1] > 0 && avgColours[1] < 255 ? true : false);
					put("Blue", avgColours[2] > 0 && avgColours[2] < 255 ? true : false);
				}
			});
		}
		{
			put("White", new HashMap<String, Boolean>() {
				private static final long serialVersionUID = 1L;
				{
					put("Red", avgColours[0] > 0 && avgColours[0] < 255 ? true : false);
					put("Green", avgColours[1] > 0 && avgColours[1] < 255 ? true : false);
					put("Blue", avgColours[2] > 0 && avgColours[2] < 255 ? true : false);
				}
			});
		}
		{
			put("Red", new HashMap<String, Boolean>() {
				private static final long serialVersionUID = 1L;
				{
					put("Red", avgColours[0] > 0 && avgColours[0] < 255 ? true : false);
					put("Green", avgColours[1] > 0 && avgColours[1] < 255 ? true : false);
					put("Blue", avgColours[2] > 0 && avgColours[2] < 255 ? true : false);
				}
			});
		}
		{
			put("Green", new HashMap<String, Boolean>() {
				private static final long serialVersionUID = 1L;
				{
					put("Red", avgColours[0] > 0 && avgColours[0] < 255 ? true : false);
					put("Green", avgColours[1] > 0 && avgColours[1] < 255 ? true : false);
					put("Blue", avgColours[2] > 0 && avgColours[2] < 255 ? true : false);
				}
			});
		}
		{
			put("Blue", new HashMap<String, Boolean>() {
				private static final long serialVersionUID = 1L;
				{
					put("Red", avgColours[0] > 0 && avgColours[0] < 255 ? true : false);
					put("Green", avgColours[1] > 0 && avgColours[1] < 255 ? true : false);
					put("Blue", avgColours[2] > 0 && avgColours[2] < 255 ? true : false);
				}
			});
		}
		{
			put("Yellow", new HashMap<String, Boolean>() {
				private static final long serialVersionUID = 1L;
				{
					put("Red", avgColours[0] > 0 && avgColours[0] < 255 ? true : false);
					put("Green", avgColours[1] > 0 && avgColours[1] < 255 ? true : false);
					put("Blue", avgColours[2] > 0 && avgColours[2] < 255 ? true : false);
				}
			});
		}
	};
	
	@Inject
	public GetColour() throws IOException {
		this.avgColours = super.getAvgColours();
	}
	
	@Override
	public int[] getAvgColours() {
		return avgColours;
	}
	
	@Override
	public String get() {
		for(String tile : colourRanges.keySet()) {
			for(String colour :  colourRanges.get(tile).keySet()) {
				if(!colourRanges.get(tile).get(colour)) {
					break;
				}
				return tile;
			}
		}
		return "Unable to compute tile...";
	}
}
