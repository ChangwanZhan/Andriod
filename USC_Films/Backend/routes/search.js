const express = require('express');
const router = express.Router();
const TMDB = require('../TMDB');
const axios = require("axios");

router.get('/:query', function(req, res) {
    let requestUrl = TMDB.getMultiSearchUrl(req.params.query);
    axios.get(requestUrl).then(response => {
        res.json(TMDB.filterMultiSearchData(response.data));
    }).catch(error => {
        console.log(error)
    })
});

module.exports = router;