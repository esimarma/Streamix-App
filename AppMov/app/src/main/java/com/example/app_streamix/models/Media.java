package com.example.app_streamix.models;

import java.util.List;

public class Media {
    private Integer id;
    private Boolean adult;
    private String overview;
    private String posterPath;
    private String title;
    private String name;
    private Double voteAverage;
    private Integer voteCount;
    private Integer runtime;
    private String releaseDate;
    private List<Genre> genres;
    private Integer numberOfEpisodes;
    private Integer numberOfSeasons;
    private String MediaType;

    public Media(Integer id, Boolean adult, String overview, String posterPath, String title, String name, Double voteAverage, Integer voteCount, Integer runtime, String releaseDate, List<Genre> genres, Integer numberOfEpisodes, Integer numberOfSeasons, String mediaType) {
        this.id = id;
        this.adult = adult;
        this.overview = overview;
        this.posterPath = posterPath;
        this.title = title;
        this.name = name;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
        this.runtime = runtime;
        this.releaseDate = releaseDate;
        this.genres = genres;
        this.numberOfEpisodes = numberOfEpisodes;
        this.numberOfSeasons = numberOfSeasons;
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
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public Integer getNumberOfEpisodes() {
        return numberOfEpisodes;
    }

    public void setNumberOfEpisodes(Integer numberOfEpisodes) {
        this.numberOfEpisodes = numberOfEpisodes;
    }

    public Integer getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public void setNumberOfSeasons(Integer numberOfSeasons) {
        this.numberOfSeasons = numberOfSeasons;
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

