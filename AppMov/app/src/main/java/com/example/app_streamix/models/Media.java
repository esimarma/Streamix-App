package com.example.app_streamix.models;

import java.util.List;

public class Media {
    private Integer id;
    private Boolean adult;
    private String overview;
    private String poster_path;
    private String title;
    private String name;
    private Double vote_average;
    private Integer vote_count;
    private Integer runtime;
    private String release_date;
    private List<Genre> genres;
    private Integer number_of_episodes;
    private Integer number_of_seasons;
    private String MediaType;

    public Media(Integer id, Boolean adult, String overview, String posterPath, String title, String name,
                 Double voteAverage, Integer voteCount, Integer runtime, String releaseDate, List<Genre> genres,
                 Integer numberOfEpisodes, Integer numberOfSeasons, String mediaType) {
        this.id = id;
        this.adult = adult;
        this.overview = overview;
        this.poster_path = posterPath;
        this.title = title;
        this.name = name;
        if(title != null && !title.isEmpty()) {
            setMediaType("movie");
        } else {
            setMediaType("tv");
        }
        this.vote_average = voteAverage;
        this.vote_count = voteCount;
        this.runtime = runtime;
        this.release_date = releaseDate;
        this.genres = genres;
        this.number_of_episodes = numberOfEpisodes;
        this.number_of_seasons = numberOfSeasons;
        MediaType = mediaType;
    }
    // Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return poster_path;
    }

    public void setPosterPath(String posterPath) {
        this.poster_path = posterPath;
    }

    public String getTitle() {
        return (title != null && !title.isEmpty()) ? title : name;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getVoteAverage() {
        return vote_average;
    }

    public void setVoteAverage(Double voteAverage) {
        this.vote_average = voteAverage;
    }

    public Integer getVoteCount() {
        return vote_count;
    }

    public void setVoteCount(Integer voteCount) {
        this.vote_count = voteCount;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    public String getReleaseDate() {
        return release_date;
    }

    public void setReleaseDate(String releaseDate) {
        this.release_date = releaseDate;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public Integer getNumberOfEpisodes() {
        return number_of_episodes;
    }

    public void setNumberOfEpisodes(Integer numberOfEpisodes) {
        this.number_of_episodes = number_of_episodes;
    }

    public Integer getNumberOfSeasons() {
        return number_of_seasons;
    }

    public void setNumberOfSeasons(Integer numberOfSeasons) {
        this.number_of_seasons = numberOfSeasons;
    }

    public String getMediaType() {
        return MediaType;
    }

    public void setMediaType(String mediaType) {
        MediaType = mediaType;
    }
}

class Genre {
    private Integer id;
    private String name;

    public Genre(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

