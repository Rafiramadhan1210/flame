package com.example.flame;

import java.util.List;

public class RawgResponseDto {
    private List<GameExternalDto> results;

    public List<GameExternalDto> getResults() { return results; }
    public void setResults(List<GameExternalDto> results) { this.results = results; }
}