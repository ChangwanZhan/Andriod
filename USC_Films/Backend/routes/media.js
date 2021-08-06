const express = require('express');
const router = express.Router();
const TMDB = require('../TMDB');
const axios = require("axios");

const moviePlaceHolder = 'https://bytes.usc.edu/cs571/s21_JSwasm00/hw/HW6/imgs/movie-placeholder.jpg';
const posterPlaceHolder = 'https://cinemaone.net/images/movie_placeholder.png';

const w500BaseUrl = 'https://image.tmdb.org/t/p/w500'
const w780BaseUrl = 'https://image.tmdb.org/t/p/w780'
const w185BaseUrl = 'https://image.tmdb.org/t/p/w185'

router.get('/popular/:mediaType', function(req, res) {
    let requestUrl = TMDB.getPopularMediaUrl(req.params.mediaType);
    axios.get(requestUrl).then(response => {
        res.json(TMDB.filterMediaData(response.data, req.params.mediaType));
    }).catch(error => {
        console.log(error)
    })
})

router.get('/top-rated/:mediaType', function(req, res) {
    let requestUrl = TMDB.getTopRatedMediaUrl(req.params.mediaType);
    axios.get(requestUrl).then(response => {
        res.json(TMDB.filterMediaData(response.data, req.params.mediaType));
    }).catch(error => {
        console.log(error)
    })
})

router.get('/trending/:mediaType', function(req, res) {
    let requestUrl = TMDB.getTrendingMediaUrl(req.params.mediaType);
    axios.get(requestUrl).then(response => {
        res.json(TMDB.filterMediaData(response.data, req.params.mediaType));
    }).catch(error => {
        console.log(error)
    })
})

router.get('/recommended/:mediaType/:id', function(req, res) {
    let requestUrl = TMDB.getRecommendedMediaUrl(req.params.mediaType, req.params.id);
    axios.get(requestUrl).then(response => {
        res.json(TMDB.filterMediaData(response.data, req.params.mediaType));
    }).catch(error => {
        console.log(error)
    })
})

router.get('/similar/:mediaType/:id', function(req, res) {
    let requestUrl = TMDB.getSimilarMediaUrl(req.params.mediaType, req.params.id);
    axios.get(requestUrl).then(response => {
        res.json(TMDB.filterMediaData(response.data, req.params.mediaType));
    }).catch(error => {
        console.log(error)
    })
})

router.get('/video/:mediaType/:id', function(req, res) {
    let requestUrl = TMDB.getMediaVideoUrl(req.params.mediaType, req.params.id);
    axios.get(requestUrl).then(response => {
        res.json(TMDB.filterVideoData(response.data));
    }).catch(error => {
        console.log(error)
    })
})

router.get('/detail/:mediaType/:id', function(req, res) {
    let requestUrl = TMDB.getMediaDetailUrl(req.params.mediaType, req.params.id);
    axios.get(requestUrl).then(response => {
        res.json(TMDB.filterDetailData(response.data, req.params.mediaType));
    }).catch(error => {
        console.log(error)
    })
})

router.get('/review/:mediaType/:id', function(req, res) {
    let requestUrl = TMDB.getMediaReviewUrl(req.params.mediaType, req.params.id);
    axios.get(requestUrl).then(response => {
        res.json(TMDB.filterReviewData(response.data));
    }).catch(error => {
        console.log(error)
    })
})

router.get('/currently-playing/:mediaType', function(req, res) {
    let requestUrl = TMDB.getCurrentPlayingUrl(req.params.mediaType);
    axios.get(requestUrl).then(response => {
        res.json(TMDB.filterCurrentPlayingMovie(req.params.mediaType, response.data));
    }).catch(error => {
        console.log(error)
    })
})

module.exports = router;