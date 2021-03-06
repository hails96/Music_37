package lsh.framgia.com.isoundcloud.data.source.remote;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import lsh.framgia.com.isoundcloud.base.asynctask.BaseFetchingAsyncTask;
import lsh.framgia.com.isoundcloud.constant.Constant;
import lsh.framgia.com.isoundcloud.constant.TrackEntity;
import lsh.framgia.com.isoundcloud.data.model.Track;

public class FetchingTrackAsyncTask extends BaseFetchingAsyncTask<String, Void, List<Track>> {
    @Override
    protected List<Track> convertInputStreamToObject(InputStream inputStream)
            throws IOException, JSONException {
        List<Track> tracks = new ArrayList<>();
        BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder tracksJson = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            tracksJson.append(line).append(Constant.NEW_LINE);
        }
        JSONObject mainObject = new JSONObject(tracksJson.toString());
        JSONArray trackArray = mainObject.getJSONArray(TrackEntity.COLLECTION);
        for (int i = 0; i < trackArray.length(); i++) {
            JSONObject trackObject = trackArray.getJSONObject(i).getJSONObject(TrackEntity.TRACK);
            tracks.add(new Track(trackObject));
        }
        return tracks;
    }
}
