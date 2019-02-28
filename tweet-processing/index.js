const OAuth = require('oauth')
const JSONStream = require('JSONStream')
const through2 = require('through2')
const { consumerKey, consumerSecret, accessToken, accessTokenSecret } = require('./secret/twitter_config')
const sampleURL = 'https://stream.twitter.com/1.1/statuses/sample.json'


const oauth = new OAuth.OAuth(
  'https://api.twitter.com/oauth/request_token',
  'https://api.twitter.com/oauth/access_token',
  consumerKey,
  consumerSecret,
  '1.0A',
  null,
  'HMAC-SHA1'
)

// https://nodejs.org/api/http.html#http_class_http_clientrequest

oauth
  .get(
    sampleURL,
    accessToken,
    accessTokenSecret
  )
  .addListener('response', function(readableStream) {
    readableStream
      .pipe(JSONStream.parse())
      .pipe(through2.obj(function(chunk, _, cb) {
        if (chunk.hasOwnProperty('created_at')) {
          const retweet = chunk.hasOwnProperty('retweeted_status')
          let tweet = {
            date: chunk.created_at,
            user: chunk.user.screen_name,
            text: chunk.truncated ? chunk.extended_tweet.full_text : chunk.text,
            retweet: !retweet ? false : {
              text: chunk.retweeted_status.text,
              user: chunk.retweeted_status.user.screen_name
            },
          }
          this.push(JSON.stringify(tweet))
        }
        cb()
      }))
      .pipe(process.stdout)
  })
  .end()
