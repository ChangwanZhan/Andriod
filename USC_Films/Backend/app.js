let express = require('express');
const path = require('path')
const cors = require('cors');

const search = require('./routes/search');
const media = require('./routes/media');
const cast = require('./routes/cast')

let app = express();
app.use(cors());

app.use(express.static(path.join(__dirname, '/dist/Frontend')));

app.use('/apis/search', search);
app.use('/apis/media', media);
app.use('/apis/cast', cast);

// app.use('/*', function (req, res) {
//     res.sendFile(path.join(__dirname + '/dist/Frontend/index.html'))
// })

app.listen(8080, function() {
    console.log("Backend Application listening at http://localhost:8080")
})



module.exports = app;