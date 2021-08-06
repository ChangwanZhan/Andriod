const apiKey = '5c2433cbb3c33dd40f9f94398c657a52';
const baseUrl = 'https://api.themoviedb.org/3/'

const w500BaseUrl = 'https://image.tmdb.org/t/p/w500'
const w780BaseUrl = 'https://image.tmdb.org/t/p/w780'

const imdbIdBaseUrl = 'https://www.imdb.com/name/'
const facebookIdBaseUrl = 'https://www.facebook.com/'
const instagramIdBaseUrl = 'https://www.instagram.com/'
const twitterIdBaseUrl = 'https://www.twitter.com/'
const avatarBaseUrl = 'https://image.tmdb.org/t/p/original'

const moviePlaceHolder = 'https://bytes.usc.edu/cs571/s21_JSwasm00/hw/HW6/imgs/movie-placeholder.jpg';
const posterPlaceHolder = 'https://cinemaone.net/images/movie_placeholder.png';
const personPlaceHolder = 'https://bytes.usc.edu/cs571/s21_JSwasm00/hw/HW6/imgs/person-placeholder.png';
const reviewPlaceHolder = 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRHnPmUvFLjjmoYWAbLTEmLLIRCPpV_OgxCVA&usqp=CAU';

const defaultVideoKey = "tzkWB85ULJY"

var months = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];

module.exports = {
    getMultiSearchUrl: function (searchQuery) {
        let params = `api_key=${apiKey}&language=en-US&query=${searchQuery}`
        return `${baseUrl}search/multi?${params}`
    },
    filterMultiSearchData: function (data) {
        let res = data.results;
        let filteredRes = [];
        res.forEach(element => {
            if (element.media_type === "movie" || element.media_type === "tv") {
                let curRes = {
                    media_type: element.media_type,
                    id: element.id,
                    vote_average: element.vote_average,
                };

                if (element.poster_path && element.poster_path !== "") {
                    curRes.poster_path = `${w500BaseUrl}${element.poster_path}`;
                } else {
                    curRes.poster_path = posterPlaceHolder;
                }

                if (element.media_type === "movie") {
                    curRes.name = element.title;
                    if (element.release_date && element.release_date !== "") {
                        curRes.year = element.release_date.split("-")[0]
                    } else {
                        curRes.year = "";
                    }
                } else {
                    curRes.name = element.name;
                    if (element.first_air_date && element.first_air_date !== "") {
                        curRes.year = element.first_air_date.split("-")[0]
                    } else {
                        curRes.year = "";
                    }
                }

                if (element.backdrop_path) {
                    curRes.backdrop_path = `${w500BaseUrl}${element.backdrop_path}`
                } else {
                    curRes.backdrop_path = moviePlaceHolder;
                }
                filteredRes.push(curRes)
            }
        })
        return filteredRes;
    },

    getPopularMediaUrl: function (mediaType) {
        let params = `api_key=${apiKey}&language=en-US&page=1`
        return `${baseUrl}${mediaType}/popular?${params}`
    },
    getTopRatedMediaUrl: function (mediaType) {
        let params = `api_key=${apiKey}&language=en-US&page=1`
        return `${baseUrl}${mediaType}/top_rated?${params}`
    },
    getTrendingMediaUrl: function (mediaType) {
        let params = `api_key=${apiKey}`
        return `${baseUrl}trending/${mediaType}/day?${params}`
    },
    getRecommendedMediaUrl: function (mediaType, id) {
        let params = `api_key=${apiKey}&language=en-US&page=1`
        return `${baseUrl}${mediaType}/${id}/recommendations?${params}`
    },
    getSimilarMediaUrl: function (mediaType, id) {
        let params = `api_key=${apiKey}&language=en-US&page=1`
        return `${baseUrl}${mediaType}/${id}/similar?${params}`
    },
    filterMediaData: function (data, mediaType) {
        let res = data.results;
        let filteredRes = [];
        res.forEach(element => {
            let curRes = {
                id: element.id,
            };
            if (element.poster_path) {
                curRes.poster_path = `${w500BaseUrl}${element.poster_path}`;
            } else {
                curRes.poster_path = posterPlaceHolder;
            }

            if (element.backdrop_path) {
                curRes.backdrop_path = `${w780BaseUrl}${element.backdrop_path}`;
            } else {
                curRes.backdrop_path = moviePlaceHolder;
            }

            if (mediaType === "movie") {
                if (mediaType === "movie") {
                    curRes.name = element.title;
                }
            } else {
                curRes.name = element.name;
            }
            curRes.media_type = mediaType;
            filteredRes.push(curRes)
        })
        return filteredRes;
    },
    getCurrentPlayingUrl: function (mediaType) {
        let params = `api_key=${apiKey}&language=en-US&page=1`
        if (mediaType === "movie") {
            return `${baseUrl}movie/now_playing?${params}`
        } else {
            return `${baseUrl}trending/tv/day?${params}`
        }

    },

    filterCurrentPlayingMovie: function (mediaType, data) {
        console.log(data);
        let res = data.results;
        let filteredRes = [];
        res.forEach(element => {
            let curRes = {
                id: element.id,

            };
            if (mediaType === "movie") {
                curRes.name = this.formatString(element.title);
            } else {
                curRes.name = this.formatString(element.name);
            }

            if (element.poster_path) {
                curRes.poster_path = `${w500BaseUrl}${element.poster_path}`;
            } else {
                curRes.poster_path = posterPlaceHolder;
            }

            if (element.backdrop_path) {
                curRes.backdrop_path = `${w780BaseUrl}${element.backdrop_path}`;
            } else {
                curRes.backdrop_path = moviePlaceHolder;
            }

            filteredRes.push(curRes)
        })
        return filteredRes.slice(0, 6);
    },

    getMediaVideoUrl: function (mediaType, id) {
        let params = `api_key=${apiKey}&language=en-US&page=1`
        return `${baseUrl}${mediaType}/${id}/videos?${params}`
    },
    filterVideoData: function (data) {
        let res = data.results;
        let teaser = [];
        let trailer = [];
        res.forEach(element => {
            let curRes = {
                site: element.site,
                type: element.type,
                name: element.name
            };
            if (element.key && element.key !== "") {
                curRes.key = element.key;
            } else {
                curRes.key = defaultVideoKey;
            }
            if (curRes.type.toLowerCase().localeCompare("trailer") === 0) {
                trailer.push(curRes);
            } else {
                teaser.push(curRes);
            }
        })
        if (trailer.length > 0) {
            return trailer[0]
        }
        else if (teaser.length > 0) {
            return teaser[0];
        }
        return {
            site: "YouTube",
            type: "",
            name: "",
            key: ""
        };
    },

    getMediaDetailUrl: function (mediaType, id) {
        let params = `api_key=${apiKey}&language=en-US&page=1`
        return `${baseUrl}${mediaType}/${id}?${params}`
    },
    filterDetailData: function (data, mediaType) {
        let spokenLanguages = []
        if (data.spoken_languages) {
            data.spoken_languages.forEach(language => {
                spokenLanguages.push(language.english_name);
            })
        }

        let genres = []
        if (data.genres) {
            data.genres.forEach(genre => {
                genres.push(genre.name);
            })
        }
        let res = {
            genres: this.formatString(genres.join(", ")),
            spoken_languages: this.formatString(spokenLanguages.join(", ")),
            overview: this.formatString(data.overview),
            vote_average: this.formatString(data.vote_average),
            tagline: this.formatString(data.tagline)
        };
        if (data.poster_path && data.poster_path !== "") {
            res.poster_path = `${w500BaseUrl}${data.poster_path}`
        } else {
            res.poster_path = posterPlaceHolder;
        }
        if (mediaType === "movie") {
            res.date = this.formatString(data.release_date);
            res.runtime = this.formatString(data.runtime);
            res.name = this.formatString(data.title);
        } else {
            res.date = this.formatString(data.first_air_date);
            if (data.episode_run_time && data.episode_run_time !== []) {
                res.runtime = this.formatString(data.episode_run_time[0]);
            }
            res.name = this.formatString(data.name);
        }
        return res;
    },


    getMediaReviewUrl: function (mediaType, id) {
        let params = `api_key=${apiKey}&language=en-US&page=1`
        return `${baseUrl}${mediaType}/${id}/reviews?${params}`
    },
    filterReviewData: function (data) {
        let reviews = [];
        data.results.forEach(element => {
            let curRes = {
                author: this.formatString(element.author),
                content: this.formatString(element.content),
                created_at: element.created_at,
                url: this.formatString(element.url),

            }
            if (element.author_details.rating && element.author_details.rating !== "") {
                curRes.rating = element.author_details.rating;
            } else {
                curRes.rating = 0;
            }

            if (element.author_details.avatar_path && element.author_details.avatar_path !== ""){
                if (element.author_details.avatar_path.slice(0,6) === "/https") {
                    curRes.avatar_path = element.author_details.avatar_path.substring(1);
                } else {
                    curRes.avatar_path = `${avatarBaseUrl}${element.author_details.avatar_path}`;
                }
            } else {
                curRes.avatar_path = reviewPlaceHolder;
            }
            reviews.push(curRes)

        })
        return reviews.slice(0, 10);
    },

    getMediaCastUrl: function (mediaType, id) {
        let params = `api_key=${apiKey}&language=en-US&page=1`
        return `${baseUrl}${mediaType}/${id}/credits?${params}`
    },

    filterCastData: function (data) {
        let res = data.cast;
        let cast = [];
        res.forEach(element => {
            let curCast = {
                id: this.formatString(element.id),
                name: this.formatString(element.name),
                character: this.formatString(element.character),
            };
            if (element.profile_path && element.profile_path !== "") {
                curCast.profile_path = `${w500BaseUrl}${element.profile_path}`;
                cast.push(curCast);
            } else {
                curCast.profile_path = personPlaceHolder;
            }
        })
        return cast;
    },

    getCastDetailUrl: function (id) {
        let params = `api_key=${apiKey}&language=en-US&page=1`
        return `${baseUrl}person/${id}?${params}`
    },
    filterCastDetailData: function (data) {
        let res = {
            birthday: this.formatString(data.birthday),
            place_of_birth: this.formatString(data.place_of_birth),
            name: this.formatString(data.name),
            homepage: this.formatString(data.homepage),
            also_known_as: this.formatString(data.also_known_as.join(", ")),
            known_for_department: this.formatString(data.known_for_department),
            biography: this.formatString(data.biography)
        }
        if (data.gender === 1) {
            res.gender = "female";
        } else if (data.gender === 2) {
            res.gender = "male";
        } else {
            res.gender = "undefined";
        }
        return res
    },

    getCastExternalIds: function (id) {
        let params = `api_key=${apiKey}&language=en-US&page=1`
        return `${baseUrl}person/${id}/external_ids?${params}`
    },
    filterCastExternalIdsData: function (data) {
        let res = {}
        if (data.imdb_id && data.imdb_id !== "") {
            res.imdb_id = `${imdbIdBaseUrl}${data.imdb_id}`
        } else {
            res.imdb_id = "";
        }

        if (data.facebook_id && data.facebook_id !== "") {
            res.facebook_id = `${facebookIdBaseUrl}${data.facebook_id}`
        } else {
            res.facebook_id = "";
        }

        if (data.instagram_id && data.instagram_id !== "") {
            res.instagram_id = `${instagramIdBaseUrl}${data.instagram_id}`
        } else {
            res.instagram_id = "";
        }

        if (data.twitter_id && data.twitter_id !== "") {
            res.twitter_id = `${twitterIdBaseUrl}${data.twitter_id}`
        } else {
            res.twitter_id = "";
        }
        return res
    },
    formatString: function (s) {
        if (s && s!=="") {
            return s;
        } else {
            return ""
        }
    },

    // formatTime: function(time) {
    //     if (time && time !== "") {
    //         let ap = "am"
    //         let date = time.split("T")[0];
    //         let t = time.split("T")[1];
    //         let year = date.split("-")[0]
    //         let month = months[parseInt(date.split("-")[1], 10) - 1];
    //         let d = parseInt(date.split("-")[2], 10)
    //
    //         let hour = parseInt(t.split(":")[0])
    //         if (hour >= 12) {
    //             ap = "pm";
    //             hour -= 12;
    //         }
    //
    //         let min = t.split(":")[1]
    //         let sec = t.split(":")[2].split(".")[0]
    //
    //         return `${month} ${d}, ${year}, ${hour}:${min}:${sec} ${ap}`;
    //     } else {
    //         return "N/A"
    //     }
    // }
};