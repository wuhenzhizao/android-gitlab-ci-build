#!/usr/bin/env bash
curl -F "file=@app/build/outputs/apk/app-debug.apk" \
     -F "uKey=b2b1a5c5cd28224b6906c1273384a94c" \
     -F "_api_key=557c047a28741e228cb6aa119dc5a5b8" http://www.pgyer.com/apiv1/app/upload