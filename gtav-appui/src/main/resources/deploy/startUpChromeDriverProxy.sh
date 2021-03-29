python \
mitmdump \
-p 5038 \
--rawtcp \
--mode reverse:http://localhost:5037  \
-s adb_proxy.py

#mitmdump -p 5038 --rawtcp --mode reverse:http://localhost:5037  -s adb_proxy.py