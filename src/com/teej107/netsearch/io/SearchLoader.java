package com.teej107.netsearch.io;

import com.teej107.netsearch.SearchExpression;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Created by teej107 on 7/31/2017.
 */
public class SearchLoader implements Consumer<Path>, Predicate<Path>
{
	private static final String URL = "url";
	private static final String COUNT = "count";
	private static final String NAMES = "names";

	private Path directory;
	private Map<String, SearchExpression> searchExpressions;
	private List<Path> failedLoad;
	private JSONParser parser;

	public SearchLoader(Path directory)
	{
		this.directory = directory;
		this.searchExpressions = new HashMap<>();
		this.failedLoad = new ArrayList<>();
		this.parser = new JSONParser();

		try
		{
			copyEmbeddedExpressions();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		try
		{
			reload();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public boolean copyEmbeddedExpressions() throws IOException
	{
		try
		{
			URI uri = getClass().getResource("/search").toURI();
			DirectoryStream<Path> stream;
			FileSystem fileSystem = null;
			try
			{
				stream = Files.newDirectoryStream(Paths.get(uri));
			}
			catch (FileSystemNotFoundException e)
			{
				fileSystem = FileSystems.newFileSystem(uri, Collections.EMPTY_MAP);
				stream = Files.newDirectoryStream(Paths.get(uri));
			}
			for (Path p : stream)
			{
				Path copyTo = directory.resolve(p.getFileName().toString());
				if (!Files.exists(copyTo))
				{
					Files.copy(p, copyTo);
				}
			}
			if(fileSystem != null)
			{
				fileSystem.close();
			}
		}
		catch (URISyntaxException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public Set<String> getSearchExpressionNames()
	{
		return searchExpressions.keySet();
	}

	public SearchExpression getSearchExpression(String name)
	{
		return searchExpressions.get(name.toLowerCase());
	}

	public void reload() throws IOException
	{
		searchExpressions.clear();
		failedLoad.clear();
		Files.walk(directory, 1).filter(this).forEach(this);
	}

	@Override
	public void accept(Path path)
	{
		try
		{
			BufferedReader br = Files.newBufferedReader(path);
			Object json = parser.parse(br);
			br.close();
			if (json instanceof JSONObject)
			{
				JSONObject object = (JSONObject) json;
				Object url = object.get(URL);
				Object count = object.get(COUNT);
				Object names = object.get(NAMES);
				if (url != null && count != null && names != null)
				{
					SearchExpression searchExpression = new SearchExpression(url.toString(), Integer.parseInt(count.toString()));
					if (names instanceof JSONArray)
					{
						for (Object o : (JSONArray) names)
						{
							searchExpressions.put(o.toString().toLowerCase(), searchExpression);
						}
					}
					else
					{
						searchExpressions.put(names.toString().toLowerCase(), searchExpression);
					}
					return;
				}
			}

		}
		catch (IOException | ParseException | NumberFormatException e)
		{
			e.printStackTrace();
		}
		failedLoad.add(path);
	}

	@Override
	public boolean test(Path path)
	{
		return !Files.isDirectory(path) && path.toString().toLowerCase().endsWith(".json");
	}
}
