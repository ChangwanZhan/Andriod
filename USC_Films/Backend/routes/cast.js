const express = require('express');
const router = express.Router();
const TMDB = require('../TMDB');
const axios = require("axios");

router.get('/media/:mediaType/:id', function(req, res) {
    let requestUrl = TMDB.getMediaCastUrl(req.params.mediaType, req.params.id);
    axios.get(requestUrl).then(response => {
        res.json(TMDB.filterCastData(response.data));
    }).catch(error => {
        console.log(error)
    })
})

router.get('/detail/:id', function(req, res) {
    let requestUrl = TMDB.getCastDetailUrl(req.params.id);
    axios.get(requestUrl).then(response => {
        res.json(TMDB.filterCastDetailData(response.data));
    }).catch(error => {
        console.log(error)
    })
})

router.get('/external-ids/:id', function(req, res) {
    let requestUrl = TMDB.getCastExternalIds(req.params.id);
    axios.get(requestUrl).then(response => {
        res.json(TMDB.filterCastExternalIdsData(response.data));
    }).catch(error => {
        console.log(error)
    })
})

module.exports = router;