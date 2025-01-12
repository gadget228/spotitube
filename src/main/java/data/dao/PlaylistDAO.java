package data.dao;

import data.domain.Playlist;
import dto.playlist.PlaylistDTO;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlaylistDAO
{
    @Resource(name = "jdbc/spotitube")
    DataSource dataSource;

    private static final String GET_ALL_QUERY = "SELECT id, name, owner_id FROM playlist";
    private static final String GET_TOTAL_DURATION_QUERY = "SELECT SUM(t.duration) AS `duration` FROM track t INNER JOIN playlist_track pt ON pt.track_id = t.id WHERE pt.playlist_id = ?";
    private static final String DELETE_QUERY = "DELETE FROM playlist WHERE id = ? AND owner_id = ?";
    private static final String CREATE_NEW_QUERY = "INSERT INTO playlist (name, owner_id) VALUES (?, ?)";
    private static final String UPDATE_NAME_QUERY = "UPDATE playlist SET name = ? WHERE id = ? AND owner_id = ?";
    private static final String IS_OWNED_BY_USER_QUERY = "SELECT 1 FROM playlist WHERE id = ? AND owner_id = ?";

    /**
     * Gets all of the playlists.
     *
     * @return All playlists.
     */
    public List<Playlist> getAll()
    {
        List<Playlist> response = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                Playlist playlist = new Playlist();
                playlist.setId(result.getInt("id"));
                playlist.setName(result.getString("name"));
                playlist.setOwnerId(result.getInt("owner_id"));

                response.add(playlist);
            }
        }
        catch (SQLException exception) {
            System.out.println("Er is iets misgegaan bij de query, fout: { "+ exception.toString()  +" }");
        }

        return response;
    }

    /**
     * Gets total duration of a list of playlists.
     *
     * @param playlists the playlists
     * @return The total duration of the tracks
     */
    public int getTotalDuration(List<Playlist> playlists)
    {
        int duration = 0;

        for (Playlist playlist : playlists) {
            try (Connection connection = dataSource.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(GET_TOTAL_DURATION_QUERY);
                statement.setInt(1, playlist.getId());
                ResultSet result = statement.executeQuery();

                if (result.next()) {
                    duration += result.getInt("duration");
                }
            }
            catch (SQLException exception) {
                System.out.println("Er is iets misgegaan bij de query, fout: { "+ exception.toString()  +" }");
            }
        }

        return duration;
    }

    /**
     * Deletes a specific playlist.
     *
     * @param playlistId The deletable playlist.
     * @param userId     The owner of the playlist.
     * @return If the deletion was successful.
     */
    public boolean delete(int playlistId, int userId)
    {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(DELETE_QUERY);
            statement.setInt(1, playlistId);
            statement.setInt(2, userId);

            return statement.executeUpdate() > 0;
        }
        catch (SQLException exception) {
            System.out.println("Er is iets misgegaan bij de query, fout: { "+ exception.toString()  +" }");
        }

        return false;
    }

    /**
     * Adds a new playlist
     *
     * @param playlist The addable playlist.
     * @return If adding was successful.
     */
    public boolean add(Playlist playlist)
    {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(CREATE_NEW_QUERY);
            statement.setString(1, playlist.getName());
            statement.setInt(2, playlist.getOwnerId());

            return statement.executeUpdate() > 0;
        }
        catch (SQLException exception) {
            System.out.println("Er is iets misgegaan bij de query, fout: { "+ exception.toString()  +" }");
        }

        return false;
    }

    /**
     * Edits the name of a specific playlist.
     *
     * @param playlistId  The editable playlist
     * @param newPlaylist The new playlist
     * @param userId      The user
     * @return If the editing was successful
     */
    public boolean editTitle(int playlistId, PlaylistDTO newPlaylist, int userId)
    {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(UPDATE_NAME_QUERY);
            statement.setString(1, newPlaylist.getName());
            statement.setInt(2, playlistId);
            statement.setInt(3, userId);

            return statement.executeUpdate() > 0;
        }
        catch (SQLException exception) {
            System.out.println("Er is iets misgegaan bij de query, fout: { "+ exception.toString()  +" }");
        }

        return false;
    }

    /**
     * Checks if a specific playlist is actually owned by a user.
     *
     * @param playlistId The ID of the playlist to check.
     * @param userId     The user to check.
     * @return If the playlist is owner by the user.
     */
    public boolean isOwnedByUser(int playlistId, int userId)
    {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(IS_OWNED_BY_USER_QUERY);
            statement.setInt(1, playlistId);
            statement.setInt(2, userId);
            ResultSet result = statement.executeQuery();

            return result.next();
        }
        catch (SQLException exception) {
            System.out.println("Er is iets misgegaan bij de query, fout: { "+ exception.toString()  +" }");
        }

        return false;
    }

    /**
     * Sets data source.
     *
     * @param dataSource the data source
     */
    public void setDataSource(DataSource dataSource)
    {
        this.dataSource = dataSource;
    }
}
