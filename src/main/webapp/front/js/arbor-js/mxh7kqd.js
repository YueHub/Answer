/*{"mac":"1:fa8d4df729cb9efc16af491c6b3318680b0a2761a6d6a346ccc15297299d6c41","created":"2011-01-10T03:52:31Z","k":"0.7.25","version":"2379537"}*/
;
(function (window, document, undefined) {
	var i = true,
	k = null,
	q = false;
	function r(a) {
		return function () {
			return this[a]
		}
	}
	var s;
	function t(a, c) {
		var b = arguments.length > 2 ? Array.prototype.slice.call(arguments, 2) : [];
		return function () {
			b.push.apply(b, arguments);
			return c.apply(a, b)
		}
	}
	function u(a, c) {
		this.p = a;
		this.i = c
	}
	s = u.prototype;
	s.createElement = function (a, c, b) {
		a = this.p.createElement(a);
		if (c)
			for (var d in c)
				if (c.hasOwnProperty(d))
					if (d == "style" && this.i.getName() == "MSIE")
						a.style.cssText = c[d];
					else
						a.setAttribute(d, c[d]);
		b && a.appendChild(this.p.createTextNode(b));
		return a
	};
	s.insertInto = function (a, c) {
		var b = this.p.getElementsByTagName(a)[0];
		if (!b)
			b = document.documentElement;
		if (b && b.lastChild) {
			b.insertBefore(c, b.lastChild);
			return i
		}
		return q
	};
	s.whenBodyExists = function (a) {
		function c() {
			document.body ? a() : setTimeout(c, 0)
		}
		c()
	};
	s.removeElement = function (a) {
		if (a.parentNode) {
			a.parentNode.removeChild(a);
			return i
		}
		return q
	};
	s.createCssLink = function (a) {
		return this.createElement("link", {
			rel : "stylesheet",
			href : a
		})
	};
	s.appendClassName = function (a, c) {
		for (var b = a.className.split(/\s+/), d = 0, e = b.length; d < e; d++)
			if (b[d] == c)
				return;
		b.push(c);
		a.className = b.join(" ").replace(/^\s+/, "")
	};
	s.removeClassName = function (a, c) {
		for (var b = a.className.split(/\s+/), d = [], e = 0, f = b.length; e < f; e++)
			b[e] != c && d.push(b[e]);
		a.className = d.join(" ").replace(/^\s+/, "").replace(/\s+$/, "")
	};
	function v(a, c, b, d, e, f, l, g) {
		this.qa = a;
		this.Ca = c;
		this.ga = b;
		this.fa = d;
		this.va = e;
		this.ua = f;
		this.ea = l;
		this.Da = g
	}
	s = v.prototype;
	s.getName = r("qa");
	s.getVersion = r("Ca");
	s.getEngine = r("ga");
	s.getEngineVersion = r("fa");
	s.getPlatform = r("va");
	s.getPlatformVersion = r("ua");
	s.getDocumentMode = r("ea");
	function w(a, c) {
		this.i = a;
		this.s = c
	}
	var aa = new v("Unknown", "Unknown", "Unknown", "Unknown", "Unknown", "Unknown", undefined, q);
	w.prototype.parse = function () {
		var a;
		if (this.i.indexOf("MSIE") != -1) {
			a = x(this, this.i, /(MSIE [\d\w\.]+)/, 1);
			if (a != "") {
				var c = a.split(" ");
				a = c[0];
				c = c[1];
				a = new v(a, c, a, c, y(this), z(this), A(this, this.s), B(this, c) >= 6)
			} else
				a = new v("MSIE", "Unknown", "MSIE", "Unknown", y(this), z(this), A(this, this.s), q)
		} else {
			if (this.i.indexOf("Opera") != -1)
				a : {
					c = a = "Unknown";
					var b = x(this, this.i, /(Presto\/[\d\w\.]+)/, 1);
					if (b != "") {
						c = b.split("/");
						a = c[0];
						c = c[1]
					} else {
						if (this.i.indexOf("Gecko") != -1)
							a = "Gecko";
						b = x(this, this.i, /rv:([^\)]+)/,
								1);
						if (b != "")
							c = b
					}
					if (this.i.indexOf("Version/") != -1) {
						b = x(this, this.i, /Version\/([\d\.]+)/, 1);
						if (b != "") {
							a = new v("Opera", b, a, c, y(this), z(this), A(this, this.s), B(this, b) >= 10);
							break a
						}
					}
					b = x(this, this.i, /Opera[\/ ]([\d\.]+)/, 1);
					a = b != "" ? new v("Opera", b, a, c, y(this), z(this), A(this, this.s), B(this, b) >= 10) : new v("Opera", "Unknown", a, c, y(this), z(this), A(this, this.s), q)
				}
			else {
				if (this.i.indexOf("AppleWebKit") != -1) {
					a = y(this);
					c = z(this);
					b = x(this, this.i, /AppleWebKit\/([\d\.]+)/, 1);
					if (b == "")
						b = "Unknown";
					var d = "Unknown";
					if (this.i.indexOf("Chrome") != -1)
						d = "Chrome";
					else if (this.i.indexOf("Safari") != -1)
						d = "Safari";
					else if (this.i.indexOf("AdobeAIR") != -1)
						d = "AdobeAIR";
					var e = "Unknown";
					if (this.i.indexOf("Version/") != -1)
						e = x(this, this.i, /Version\/([\d\.\w]+)/, 1);
					else if (d == "Chrome")
						e = x(this, this.i, /Chrome\/([\d\.]+)/, 1);
					else if (d == "AdobeAIR")
						e = x(this, this.i, /AdobeAIR\/([\d\.]+)/, 1);
					var f = q;
					if (d == "AdobeAIR") {
						f = x(this, e, /\d+\.(\d+)/, 1);
						f = B(this, e) > 2 || B(this, e) == 2 && parseInt(f, 10) >= 5
					} else {
						f = x(this, b, /\d+\.(\d+)/, 1);
						f = B(this, b) >=
							526 || B(this, b) >= 525 && parseInt(f, 10) >= 13
					}
					a = new v(d, e, "AppleWebKit", b, a, c, A(this, this.s), f)
				} else {
					if (this.i.indexOf("Gecko") != -1) {
						c = a = "Unknown";
						d = q;
						if (this.i.indexOf("Firefox") != -1) {
							a = "Firefox";
							b = x(this, this.i, /Firefox\/([\d\w\.]+)/, 1);
							if (b != "") {
								d = x(this, b, /\d+\.(\d+)/, 1);
								c = b;
								d = b != "" && B(this, b) >= 3 && parseInt(d, 10) >= 5
							}
						} else if (this.i.indexOf("Mozilla") != -1)
							a = "Mozilla";
						b = x(this, this.i, /rv:([^\)]+)/, 1);
						if (b == "")
							b = "Unknown";
						else if (!d) {
							d = B(this, b);
							e = parseInt(x(this, b, /\d+\.(\d+)/, 1), 10);
							f = parseInt(x(this,
										b, /\d+\.\d+\.(\d+)/, 1), 10);
							d = d > 1 || d == 1 && e > 9 || d == 1 && e == 9 && f >= 2 || b.match(/1\.9\.1b[123]/) != k || b.match(/1\.9\.1\.[\d\.]+/) != k
						}
						a = new v(a, c, "Gecko", b, y(this), z(this), A(this, this.s), d)
					} else
						a = aa;
					a = a
				}
				a = a
			}
			a = a
		}
		return a
	};
	function y(a) {
		var c = x(a, a.i, /(iPod|iPad|iPhone|Android)/, 1);
		if (c != "")
			return c;
		a = x(a, a.i, /(Linux|Mac_PowerPC|Macintosh|Windows)/, 1);
		if (a != "") {
			if (a == "Mac_PowerPC")
				a = "Macintosh";
			return a
		}
		return "Unknown"
	}
	function z(a) {
		var c = x(a, a.i, /(OS X|Windows NT|Android) ([^;)]+)/, 2);
		if (c)
			return c;
		if (c = x(a, a.i, /(iPhone )?OS ([\d_]+)/, 2))
			return c;
		if (a = x(a, a.i, /Linux ([i\d]+)/, 1))
			return a;
		return "Unknown"
	}
	function B(a, c) {
		var b = x(a, c, /(\d+)/, 1);
		if (b != "")
			return parseInt(b, 10);
		return -1
	}
	function x(a, c, b, d) {
		if ((a = c.match(b)) && a[d])
			return a[d];
		return ""
	}
	function A(a, c) {
		if (c.documentMode)
			return c.documentMode
	}
	function ba(a, c, b, d) {
		this.j = a;
		this.l = c;
		this.L = b;
		this.o = d || "wf";
		this.n = new C("-")
	}
	function D(a) {
		a.j.removeClassName(a.l, a.n.m(a.o, "loading"));
		a.j.appendClassName(a.l, a.n.m(a.o, "inactive"));
		F(a, "inactive")
	}
	function F(a, c, b, d) {
		a.L[c] && a.L[c](b, d)
	}
	function G(a, c, b, d, e) {
		this.j = a;
		this.v = c;
		this.w = b;
		this.q = d;
		this.G = e;
		this.N = 0;
		this.ba = this.V = q
	}
	G.prototype.watch = function (a, c, b, d) {
		for (var e = a.length, f = 0; f < e; f++) {
			var l = a[f];
			c[l] || (c[l] = ["n4"]);
			this.N += c[l].length
		}
		if (d)
			this.V = d;
		for (f = 0; f < e; f++) {
			l = a[f];
			d = c[l];
			for (var g = b[l], h = 0, m = d.length; h < m; h++) {
				var n = d[h],
				p = this.v,
				j = l,
				o = n;
				p.j.appendClassName(p.l, p.n.m(p.o, j, o, "loading"));
				F(p, "fontloading", j, o);
				p = t(this, this.ha);
				j = t(this, this.ia);
				new H(p, j, this.j, this.w, this.q, this.G, l, n, g)
			}
		}
	};
	G.prototype.ha = function (a, c) {
		var b = this.v;
		b.j.removeClassName(b.l, b.n.m(b.o, a, c, "loading"));
		b.j.appendClassName(b.l, b.n.m(b.o, a, c, "active"));
		F(b, "fontactive", a, c);
		this.ba = i;
		I(this)
	};
	G.prototype.ia = function (a, c) {
		var b = this.v;
		b.j.removeClassName(b.l, b.n.m(b.o, a, c, "loading"));
		b.j.appendClassName(b.l, b.n.m(b.o, a, c, "inactive"));
		F(b, "fontinactive", a, c);
		I(this)
	};
	function I(a) {
		if (--a.N == 0 && a.V)
			if (a.ba) {
				a = a.v;
				a.j.removeClassName(a.l, a.n.m(a.o, "loading"));
				a.j.appendClassName(a.l, a.n.m(a.o, "active"));
				F(a, "active")
			} else
				D(a.v)
	}
	function H(a, c, b, d, e, f, l, g, h) {
		this.da = a;
		this.la = c;
		this.j = b;
		this.w = d;
		this.q = e;
		this.G = f;
		this.pa = new J;
		this.ka = new K;
		this.Q = l;
		this.P = g;
		this.ja = h || "BESs";
		this.sa = L(this, "arial,'URW Gothic L',sans-serif");
		this.ta = L(this, "Georgia,'Century Schoolbook L',serif");
		this.Z = M(this, "arial,'URW Gothic L',sans-serif");
		this.$ = M(this, "Georgia,'Century Schoolbook L',serif");
		this.Ba = f();
		this.M()
	}
	H.prototype.M = function () {
		var a = this.w.H(this.$);
		if (this.sa != this.w.H(this.Z) || this.ta != a)
			N(this, this.da);
		else
			this.G() - this.Ba < 5E3 ? ca(this) : N(this, this.la)
	};
	function ca(a) {
		a.q(function (c, b) {
			return function () {
				b.call(c)
			}
		}
			(a, a.M), 50)
	}
	function N(a, c) {
		a.j.removeElement(a.Z);
		a.j.removeElement(a.$);
		c(a.Q, a.P)
	}
	function L(a, c) {
		var b = M(a, c, i),
		d = a.w.H(b);
		a.j.removeElement(b);
		return d
	}
	function M(a, c, b) {
		var d = a.ka.expand(a.P);
		c = a.j.createElement("span", {
				style : "position:absolute;top:-999px;font-size:300px;font-family:" + (b ? "" : a.pa.quote(a.Q) + ",") + c + ";" + d
			}, a.ja);
		a.j.insertInto("body", c);
		return c
	}
	function C(a) {
		this.na = a || "-"
	}
	C.prototype.m = function () {
		for (var a = [], c = 0; c < arguments.length; c++)
			a.push(arguments[c].replace(/[\W_]+/g, "").toLowerCase());
		return a.join(this.na)
	};
	function J() {
		this.Y = '"'
	}
	J.prototype.quote = function (a) {
		var c = [];
		a = a.split(/,\s*/);
		for (var b = 0; b < a.length; b++) {
			var d = a[b].replace(/['"]/g, "");
			d.indexOf(" ") == -1 ? c.push(d) : c.push(this.Y + d + this.Y)
		}
		return c.join(",")
	};
	function K() {
		this.X = da;
		this.z = ea
	}
	var da = ["font-style", "font-weight"],
	ea = {
		"font-style" : [["n", "normal"], ["i", "italic"], ["o", "oblique"]],
		"font-weight" : [["1", "100"], ["2", "200"], ["3", "300"], ["4", "400"], ["5", "500"], ["6", "600"], ["7", "700"], ["8", "800"], ["9", "900"], ["4", "normal"], ["7", "bold"]]
	};
	function O(a, c, b) {
		this.ma = a;
		this.wa = c;
		this.z = b
	}
	O.prototype.expand = function (a, c) {
		for (var b = 0; b < this.z.length; b++)
			if (c == this.z[b][0]) {
				a[this.ma] = this.wa + ":" + this.z[b][1];
				break
			}
	};
	K.prototype.expand = function (a) {
		if (a.length != 2)
			return k;
		for (var c = [k, k], b = 0, d = this.X.length; b < d; b++) {
			var e = this.X[b];
			(new O(b, e, this.z[e])).expand(c, a.substr(b, 1))
		}
		return c[0] && c[1] ? c.join(";") + ";" : k
	};
	function P(a, c) {
		this.p = a;
		this.i = c
	}
	P.prototype = u.prototype;
	P.prototype.isHttps = function () {
		return this.p.location.protocol == "https:"
	};
	P.prototype.loadScript = function (a, c) {
		var b = this.p.getElementsByTagName("head")[0];
		if (b) {
			var d = this.p.createElement("script");
			d.src = a;
			var e = q;
			d.onload = d.onreadystatechange = function () {
				if (!e && (!this.readyState || this.readyState == "loaded" || this.readyState == "complete")) {
					e = i;
					c && c();
					d.onload = d.onreadystatechange = k;
					d.parentNode.tagName == "HEAD" && b.removeChild(d)
				}
			};
			b.appendChild(d)
		}
	};
	P.prototype.createStyle = function (a) {
		var c = this.p.createElement("style");
		c.setAttribute("type", "text/css");
		if (c.styleSheet)
			c.styleSheet.cssText = a;
		else
			c.appendChild(document.createTextNode(a));
		return c
	};
	function fa(a) {
		for (var c = a.za.join(","), b = [], d = 0; d < a.O.length; d++) {
			var e = a.O[d];
			b.push(e.name + ":" + e.value + ";")
		}
		return c + "{" + b.join("") + "}"
	}
	function Q(a, c) {
		this.A = a;
		this.R = c
	}
	Q.prototype.watch = function (a, c, b) {
		var d = [],
		e = {};
		d.push(this.A);
		e[this.A] = this.R[c.getStylesheetFormatId()] || [];
		a.watch(d, e, {}, b)
	};
	function R(a, c, b) {
		this.Aa = a;
		this.I = c;
		this.xa = b
	}
	R.prototype.buildUrl = function (a, c) {
		var b = this.Aa && a ? "https:" : "http:",
		d = typeof this.I == "function" ? this.I(b) : this.I;
		return b + "//" + d + this.xa + c
	};
	function ga(a, c, b) {
		if (a.S) {
			var d = function () {
				try {
					c._gat._getTracker("UA-8850781-3")._trackPageview();
					c.tkKitsTracked || (c.tkKitsTracked = 0);
					c.tkKitsTracked++
				} catch (e) {}

			};
			if (c._gat)
				d();
			else {
				a = a.S.buildUrl(b.isHttps(), "/ga.js");
				b.loadScript(a, d)
			}
		}
	}
	function S(a, c, b) {
		this.u = a;
		this.aa = c;
		this.U = b
	}
	S.prototype.getId = r("u");
	S.prototype.getStylesheetFormatId = r("aa");
	S.prototype.isUserAgent = function (a) {
		return this.U ? this.U(a.getName(), a.getVersion(), a.getEngine(), a.getEngineVersion(), a.getPlatform(), a.getPlatformVersion(), a.getDocumentMode()) : q
	};
	S.prototype.buildCssUrl = function (a, c, b, d) {
		b = "/" + b + "-" + this.aa + ".css";
		if (d)
			b += "?" + d;
		return c.buildUrl(a, b)
	};
	function T() {
		this.r = []
	}
	T.prototype.addBrowser = function (a) {
		this.getBrowserById(a.getId()) || this.r.push(a)
	};
	T.prototype.getBrowserById = function (a) {
		for (var c = 0; c < this.r.length; c++) {
			var b = this.r[c];
			if (a === b.getId())
				return b
		}
		return k
	};
	T.prototype.findBrowser = function (a) {
		for (var c = 0; c < this.r.length; c++) {
			var b = this.r[c];
			if (b.isUserAgent(a))
				return b
		}
		return k
	};
	T.prototype.addBrowsersToBrowserSet = function (a) {
		for (var c = 0; c < this.r.length; c++)
			a.addBrowser(this.r[c])
	};
	function U(a) {
		this.u = a;
		this.D = new T;
		this.t = [];
		this.F = []
	}
	s = U.prototype;
	s.getId = r("u");
	s.setSecurityToken = function (a) {
		this.ya = a
	};
	s.setNestedUrl = function (a) {
		this.ra = a
	};
	s.setKitOptions = function (a) {
		this.B = a
	};
	s.addBrowser = function (a) {
		this.D.addBrowser(a)
	};
	s.addFontFamily = function (a) {
		this.t.push(a)
	};
	s.addCssRule = function (a) {
		this.F.push(a)
	};
	s.supportsBrowser = function (a) {
		return !!this.D.getBrowserById(a.getId())
	};
	s.addBrowsersToBrowserSet = function (a) {
		this.D.addBrowsersToBrowserSet(a)
	};
	s.init = function (a) {
		for (var c = [], b = 0; b < this.F.length; b++)
			c.push(fa(this.F[b]));
		a.insertInto("head", a.createStyle(c.join("")))
	};
	s.load = function (a, c, b, d) {
		var e = b.buildCssUrl(a.isHttps(), this.ra, this.u, this.ya);
		a.insertInto("head", a.createCssLink(e));
		c && a.whenBodyExists(function (f, l, g, h) {
			return function () {
				for (var m = 0; m < f.t.length; m++)
					f.t[m].watch(l, g, h && m == f.t.length - 1)
			}
		}
			(this, c, b, d))
	};
	s.collectFontFamilies = function (a, c, b) {
		for (var d = 0; d < this.t.length; d++) {
			var e = this.t[d],
			f = a,
			l = b;
			c.push(e.A);
			l[e.A] = e.R[f.getStylesheetFormatId()] || []
		}
	};
	s.performOptionalActions = function (a, c, b) {
		this.B && b.whenBodyExists(function (d, e, f, l) {
			return function () {
				ga(d.B, e, l);
				var g = d.B,
				h = d.u;
				if (g.W) {
					g = g.W.buildUrl(l.isHttps(), "/" + h + ".js?" + +new Date);
					l.loadScript(g)
				}
				h = d.B;
				g = d.u;
				if (h.K) {
					h = h.K.m(g, f, l);
					h.setAttribute("id", "typekit-badge-" + g);
					l.insertInto("body", h)
				}
			}
		}
			(this, a, c, b))
	};
	function V(a, c, b, d, e) {
		this.oa = a;
		this.j = c;
		this.i = b;
		this.l = d;
		this.q = e;
		this.k = []
	}
	V.prototype.C = function (a) {
		this.k.push(a)
	};
	V.prototype.load = function (a, c) {
		var b = a,
		d = c || {};
		if (typeof b == "string")
			b = [b];
		else if (b && b.length)
			b = b;
		else {
			d = b || {};
			b = []
		}
		if (b.length)
			for (var e = this, f = b.length, l = 0; l < b.length; l++)
				this.ca(b[l], function () {
					--f == 0 && e.J(d)
				});
		else
			this.J(d)
	};
	V.prototype.ca = function (a, c) {
		this.j.loadScript(this.oa.buildUrl(this.j.isHttps(), "/" + a + ".js"), c)
	};
	V.prototype.J = function (a) {
		if (a.userAgent)
			this.i = (new w(a.userAgent, document)).parse();
		a = new ba(this.j, this.l, a);
		for (var c = new T, b = 0; b < this.k.length; b++)
			this.k[b].addBrowsersToBrowserSet(c);
		c = c.findBrowser(this.i);
		for (b = 0; b < this.k.length; b++)
			this.k[b].init(this.j);
		if (c) {
			a.j.appendClassName(a.l, a.n.m(a.o, "loading"));
			F(a, "loading");
			ha(this, c, a)
		} else
			D(a);
		this.k = []
	};
	function ha(a, c, b) {
		b = new G(a.j, b, {
				H : function (f) {
					return f.offsetWidth
				}
			}, a.q, function () {
				return +new Date
			});
		for (var d = 0; d < a.k.length; d++) {
			var e = a.k[d];
			if (e.supportsBrowser(c)) {
				e.load(a.j, b, c, d == a.k.length - 1);
				e.performOptionalActions(window, a.i, a.j)
			}
		}
	}
	function W(a, c, b) {
		this.T = a;
		this.j = c;
		this.q = b;
		this.k = []
	}
	W.prototype.C = function (a) {
		this.k.push(a)
	};
	W.prototype.load = function () {
		var a = this.T.__webfonttypekitmodule__;
		if (a)
			for (var c = 0; c < this.k.length; c++) {
				var b = this.k[c],
				d = a[b.getId()];
				if (d) {
					var e = this.j,
					f = this.q;
					d(function (l, g, h) {
						var m = new T;
						b.addBrowsersToBrowserSet(m);
						g = [];
						var n = {};
						if (m = m.findBrowser(l)) {
							b.init(e);
							b.load(e, k, m, f);
							b.collectFontFamilies(m, g, n);
							b.performOptionalActions(window, l, e, f)
						}
						h(!!m, g, n)
					})
				}
			}
	};
	var X = new T;
	X.addBrowser(new S("chrome6-win2003-win7-winvista-winxp", "e", function (a, c, b, d, e, f, l) {
			var g = q;
			g = (g = (g = (g = g || (e == "Windows" && f == "5.1" ? i : q)) || (e == "Windows" && f == "5.2" ? i : q)) || (e == "Windows" && f == "6.0" ? i : q)) || (e == "Windows" && f == "6.1" ? i : q);
			if (!g)
				return q;
			return function (h, m) {
				if (h == "Chrome") {
					var n = /([0-9]+.[0-9]+).([0-9]+).([0-9]+)/.exec(m);
					if (n)
						if (parseFloat(n[1]) >= 6)
							return i
				}
			}
			(a, c, b, d, e, f, l)
		}));
	X.addBrowser(new S("opera-linux-osx-win2003-win7-winvista-winxp", "b", function (a, c, b, d, e, f, l) {
			var g = q;
			g = (g = (g = (g = (g = (g = g || (e == "Windows" && f == "5.1" ? i : q)) || (e == "Windows" && f == "5.2" ? i : q)) || (e == "Windows" && f == "6.0" ? i : q)) || (e == "Windows" && f == "6.1" ? i : q)) || function (h, m, n, p, j, o) {
				h = /^([0-9]+)(_|.)([0-9]+)/.exec(o);
				if (j == "Macintosh" && h) {
					j = parseInt(h[1], 10);
					o = parseInt(h[3], 10);
					if (j == 10)
						return o > 3;
					else if (j > 10)
						return i
				} else
					return j == "Macintosh" && o == "Unknown" ? i : q
			}
				(a, c, b, d, e, f, l)) || (e == "Ubuntu" || e == "Linux" ? i : q);
			if (!g)
				return q;
			a = a == "Opera" ? parseFloat(c) >= 10.54 : q;
			return a
		}));
	X.addBrowser(new S("ff35-linux-osx-win2003-win7-winvista-winxp", "b", function (a, c, b, d, e, f, l) {
			var g = q;
			g = (g = (g = (g = (g = (g = g || (e == "Windows" && f == "5.1" ? i : q)) || (e == "Windows" && f == "5.2" ? i : q)) || (e == "Windows" && f == "6.0" ? i : q)) || (e == "Windows" && f == "6.1" ? i : q)) || function (h, m, n, p, j, o) {
				h = /^([0-9]+)(_|.)([0-9]+)/.exec(o);
				if (j == "Macintosh" && h) {
					j = parseInt(h[1], 10);
					o = parseInt(h[3], 10);
					if (j == 10)
						return o > 3;
					else if (j > 10)
						return i
				} else
					return j == "Macintosh" && o == "Unknown" ? i : q
			}
				(a, c, b, d, e, f, l)) || (e == "Ubuntu" || e == "Linux" ? i : q);
			if (!g)
				return q;
			return function (h, m, n, p) {
				if (n == "Gecko") {
					h = /1.9.1b[1-3]{1}/;
					return /1.9.1/.test(p) && !h.test(p)
				}
				return q
			}
			(a, c, b, d, e, f, l)
		}));
	X.addBrowser(new S("ff36-linux-osx-win2003-win7-winvista-winxp", "e", function (a, c, b, d, e, f, l) {
			var g = q;
			g = (g = (g = (g = (g = (g = g || (e == "Windows" && f == "5.1" ? i : q)) || (e == "Windows" && f == "5.2" ? i : q)) || (e == "Windows" && f == "6.0" ? i : q)) || (e == "Windows" && f == "6.1" ? i : q)) || function (h, m, n, p, j, o) {
				h = /^([0-9]+)(_|.)([0-9]+)/.exec(o);
				if (j == "Macintosh" && h) {
					j = parseInt(h[1], 10);
					o = parseInt(h[3], 10);
					if (j == 10)
						return o > 3;
					else if (j > 10)
						return i
				} else
					return j == "Macintosh" && o == "Unknown" ? i : q
			}
				(a, c, b, d, e, f, l)) || (e == "Ubuntu" || e == "Linux" ? i : q);
			if (!g)
				return q;
			return function (h, m, n, p) {
				if (n == "Gecko")
					if (m = /([0-9]+.[0-9]+)(.([0-9]+)|)/.exec(p)) {
						h = parseFloat(m[1]);
						m = parseInt(m[3], 10);
						return h > 1.9 || h >= 1.9 && m > 1
					}
				return q
			}
			(a, c, b, d, e, f, l)
		}));
	X.addBrowser(new S("air-linux-osx-win", "b", function (a, c, b, d, e, f, l) {
			var g = q;
			g = (g = (g = g || (e == "Windows" && f == "Unknown" ? i : q)) || function (h, m, n, p, j, o) {
				h = /^([0-9]+)(_|.)([0-9]+)/.exec(o);
				if (j == "Macintosh" && h) {
					j = parseInt(h[1], 10);
					o = parseInt(h[3], 10);
					if (j == 10)
						return o > 3;
					else if (j > 10)
						return i
				} else
					return j == "Macintosh" && o == "Unknown" ? i : q
			}
				(a, c, b, d, e, f, l)) || (e == "Ubuntu" || e == "Linux" ? i : q);
			if (!g)
				return q;
			return function (h, m) {
				if (h == "AdobeAIR") {
					var n = /([0-9]+.[0-9]+)/.exec(m);
					if (n)
						return parseFloat(n[1]) >= 2.5
				}
				return q
			}
			(a,
				c, b, d, e, f, l)
		}));
	X.addBrowser(new S("ie9-win7", "g", function (a, c, b, d, e, f, l) {
			var g = q;
			g = g || (e == "Windows" && f == "6.1" ? i : q);
			if (!g)
				return q;
			return function (h, m, n, p, j, o, E) {
				if (h == "MSIE") {
					if (h = /([0-9]+.[0-9]+)/.exec(m))
						return parseFloat(h[1]) >= 9 && E >= 9;
					return q
				}
			}
			(a, c, b, d, e, f, l)
		}));
	X.addBrowser(new S("chrome6-linux-osx", "d", function (a, c, b, d, e, f, l) {
			var g = q;
			g = (g = g || function (h, m, n, p, j, o) {
				h = /^([0-9]+)(_|.)([0-9]+)/.exec(o);
				if (j == "Macintosh" && h) {
					j = parseInt(h[1], 10);
					o = parseInt(h[3], 10);
					if (j == 10)
						return o > 3;
					else if (j > 10)
						return i
				} else
					return j == "Macintosh" && o == "Unknown" ? i : q
			}
				(a, c, b, d, e, f, l)) || (e == "Ubuntu" || e == "Linux" ? i : q);
			if (!g)
				return q;
			return function (h, m) {
				if (h == "Chrome") {
					var n = /([0-9]+.[0-9]+).([0-9]+).([0-9]+)/.exec(m);
					if (n)
						if (parseFloat(n[1]) >= 6)
							return i
				}
			}
			(a, c, b, d, e, f, l)
		}));
	X.addBrowser(new S("ie9-winvista", "h", function (a, c, b, d, e, f, l) {
			var g = q;
			g = g || (e == "Windows" && f == "6.0" ? i : q);
			if (!g)
				return q;
			return function (h, m, n, p, j, o, E) {
				if (h == "MSIE") {
					if (h = /([0-9]+.[0-9]+)/.exec(m))
						return parseFloat(h[1]) >= 9 && E >= 9;
					return q
				}
			}
			(a, c, b, d, e, f, l)
		}));
	X.addBrowser(new S("safari-osx-win2003-win7-winvista-winxp", "b", function (a, c, b, d, e, f, l) {
			var g = q;
			g = (g = (g = (g = (g = g || (e == "Windows" && f == "5.1" ? i : q)) || (e == "Windows" && f == "5.2" ? i : q)) || (e == "Windows" && f == "6.0" ? i : q)) || (e == "Windows" && f == "6.1" ? i : q)) || function (h, m, n, p, j, o) {
				h = /^([0-9]+)(_|.)([0-9]+)/.exec(o);
				if (j == "Macintosh" && h) {
					j = parseInt(h[1], 10);
					o = parseInt(h[3], 10);
					if (j == 10)
						return o > 3;
					else if (j > 10)
						return i
				} else
					return j == "Macintosh" && o == "Unknown" ? i : q
			}
			(a, c, b, d, e, f, l);
			if (!g)
				return q;
			return function (h, m, n, p, j) {
				if (h ==
					"Safari" && n == "AppleWebKit" || h == "Unknown" && n == "AppleWebKit" && (j == "iPhone" || j == "iPad"))
					if (h = /([0-9]+.[0-9]+)/.exec(p))
						return parseFloat(h[1]) >= 525.13;
				return q
			}
			(a, c, b, d, e, f, l)
		}));
	X.addBrowser(new S("ie-win2003-win7-winvista-winxp", "c", function (a, c, b, d, e, f, l) {
			var g = q;
			g = (g = (g = (g = g || (e == "Windows" && f == "5.1" ? i : q)) || (e == "Windows" && f == "5.2" ? i : q)) || (e == "Windows" && f == "6.0" ? i : q)) || (e == "Windows" && f == "6.1" ? i : q);
			if (!g)
				return q;
			return function (h, m, n, p, j, o, E) {
				if (h == "MSIE") {
					if (h = /([0-9]+.[0-9]+)/.exec(m))
						return parseFloat(h[1]) >= 6 && (E === undefined || E < 9);
					return q
				}
			}
			(a, c, b, d, e, f, l)
		}));
	X.addBrowser(new S("chrome-linux-osx-win2003-win7-winvista-winxp", "b", function (a, c, b, d, e, f, l) {
			var g = q;
			g = (g = (g = (g = (g = (g = g || (e == "Windows" && f == "5.1" ? i : q)) || (e == "Windows" && f == "5.2" ? i : q)) || (e == "Windows" && f == "6.0" ? i : q)) || (e == "Windows" && f == "6.1" ? i : q)) || function (h, m, n, p, j, o) {
				h = /^([0-9]+)(_|.)([0-9]+)/.exec(o);
				if (j == "Macintosh" && h) {
					j = parseInt(h[1], 10);
					o = parseInt(h[3], 10);
					if (j == 10)
						return o > 3;
					else if (j > 10)
						return i
				} else
					return j == "Macintosh" && o == "Unknown" ? i : q
			}
				(a, c, b, d, e, f, l)) || (e == "Ubuntu" || e == "Linux" ? i : q);
			if (!g)
				return q;
			return function (h, m) {
				if (h == "Chrome") {
					var n = /([0-9]+.[0-9]+).([0-9]+).([0-9]+)/.exec(m);
					if (n) {
						var p = parseFloat(n[1]),
						j = parseInt(n[2], 10);
						n = parseInt(n[3], 10);
						if (p >= 6)
							return q;
						else if (p > 4)
							return i;
						else if (p == 4 && j > 249)
							return i;
						else if (p == 4 && j == 249 && n >= 4)
							return i
					}
				}
				return q
			}
			(a, c, b, d, e, f, l)
		}));
	X.addBrowser(new S("safari-android", "a", function (a, c, b, d, e, f, l) {
			var g = q;
			g = g || function (h, m, n, p, j, o) {
				h = /([0-9]+).([0-9]+)/.exec(o);
				if (j == "Android" && h) {
					j = parseInt(h[1]);
					h = parseInt(h[2]);
					return j > 2 || j == 2 && h >= 2
				} else
					return q
			}
			(a, c, b, d, e, f, l);
			if (!g)
				return q;
			return function (h, m, n, p, j) {
				if (h == "Safari" && n == "AppleWebKit" || h == "Unknown" && n == "AppleWebKit" && (j == "iPhone" || j == "iPad"))
					if (h = /([0-9]+.[0-9]+)/.exec(p))
						return parseFloat(h[1]) >= 525.13;
				return q
			}
			(a, c, b, d, e, f, l)
		}));
	if (!window.Typekit) {
		var ia = new R(q, "use.typekit.com", "/"),
		ja = (new w(navigator.userAgent, document)).parse(),
		ka = new P(document, ja),
		la = function (a, c) {
			setTimeout(a, c)
		},
		Y = new V(ia, ka, ja, document.documentElement, la),
		Z = new W(window, ka, la);
		window.Typekit = Y;
		window.Typekit.load = Y.load;
		window.Typekit.addKit = Y.C
	}
	var ma,
	na,
	$;
	ma = new R(q, "use.typekit.com", "/k");
	na = new function (a, c, b) {
		this.K = a;
		this.S = c;
		this.W = b
	}
	(k, k, k);
	$ = new U("mxh7kqd");
	$.setSecurityToken("3bb2a6e53c9684ffdc9a9bf6135b2a629dfd401408a09a37c12b617a842b331f35c2bb8df74f8d96e8bcfc067b07304de2f7a5a14d2f15415701aae9c360bd833480c362644f413a7ea04f43fbb6f216f8e44dfde2185620695a5693d75b0720e0b18d2b853ab78a87ea454ae0");
	$.setNestedUrl(ma);
	$.setKitOptions(na);
	$.addFontFamily(new Q('"athelas-1","athelas-2"', {
			a : ["n4", "i4", "n7"],
			b : ["n4", "i4", "n7"],
			c : ["n4", "i4", "n7"],
			d : ["n4", "i4", "n7"],
			e : ["n4", "i4", "n7"],
			f : ["n4", "i4", "n7"],
			g : ["n4", "i4", "n7"],
			h : ["n4", "i4", "n7"]
		}));
	$.addCssRule(new function (a, c) {
		this.za = a;
		this.O = c
	}
		([".tk-athelas"], [{
					value : '"athelas-1","athelas-2",serif',
					name : "font-family"
				}
			]));
	$.addBrowser(X.getBrowserById("air-linux-osx-win"));
	$.addBrowser(X.getBrowserById("chrome-linux-osx-win2003-win7-winvista-winxp"));
	$.addBrowser(X.getBrowserById("chrome6-linux-osx"));
	$.addBrowser(X.getBrowserById("chrome6-win2003-win7-winvista-winxp"));
	$.addBrowser(X.getBrowserById("ff35-linux-osx-win2003-win7-winvista-winxp"));
	$.addBrowser(X.getBrowserById("ff36-linux-osx-win2003-win7-winvista-winxp"));
	$.addBrowser(X.getBrowserById("ie-win2003-win7-winvista-winxp"));
	$.addBrowser(X.getBrowserById("ie9-win7"));
	$.addBrowser(X.getBrowserById("ie9-winvista"));
	$.addBrowser(X.getBrowserById("opera-linux-osx-win2003-win7-winvista-winxp"));
	$.addBrowser(X.getBrowserById("safari-android"));
	$.addBrowser(X.getBrowserById("safari-osx-win2003-win7-winvista-winxp"));
	if (Z && Z.T.__webfonttypekitmodule__) {
		Z.C($);
		Z.load()
	} else
		window.Typekit.addKit($);
})(this, document);
window.Typekit.config = {
	"p" : "//p.typekit.net/p.gif?s=1&k=mxh7kqd&ht=tk&h={host}&f=1693.1692.1694&a=234380&_={_}"
}; ;
(function (window, document, undefined) {
	if (!document.querySelector) {
		document.documentElement.className += " wf-inactive";
		return;
	}
	function f(a) {
		this.b = a
	}
	f.prototype.toString = function () {
		return encodeURIComponent(h(this.b))
	};
	function k(a, b) {
		this.b = b
	}
	k.prototype.toString = function () {
		for (var a = [], b = 0; b < this.b.length; b++)
			for (var d = this.b[b], c = d.f, d = d.f, g = 0; g < c.length; g++) {
				var e;
				a : {
					for (e = 0; e < d.length; e++)
						if (c[g] === d[e]) {
							e = !0;
							break a
						}
					e = !1
				}
				a.push(e ? 1 : 0)
			}
		a = a.join("");
		a = a.replace(/^0+/, "");
		b = [];
		for (c = a.length; 0 < c; c -= 4)
			b.unshift(parseInt(a.slice(0 > c - 4 ? 0 : c - 4, c), 2).toString(16));
		return b.join("")
	};
	var l = {};
	l.a = l.d = l.l = l.j = function () {
		return []
	};
	l.i = function (a, b, d) {
		return [new f(a), new k(0, d)]
	};
	l.k = function (a) {
		return [new f(a)]
	};
	function m(a) {
		this.c = a
	}
	m.prototype.b = function () {
		var a = {
			host : n,
			_ : (+new Date).toString()
		},
		b = this.c.replace(/\{\/?([^*}]*)(\*?)\}/g, function (b, c, g) {
				return g && a[c] ? "/" + a[c].join("/") : a[c] || ""
			});
		b.match(/^\/\//) && (b = "https:" + b);
		return b.replace(/\/*\?*($|\?)/, "$1")
	};
	function p() {
		var a = window;
		this.b = this.c = a
	}
	function h(a) {
		return a.b.location.hostname || a.c.location.hostname
	};
	window.Typekit || (window.Typekit = {});
	var q = window.Typekit.config;
	if (q.p) {
		var r = new m(q.p),
		n = encodeURIComponent(h(new p)),
		t = new Image(1, 1),
		u = !1;
		t.src = r.b();
		t.onload = function () {
			u = !0;
			t.onload = null
		};
		setTimeout(function () {
			u || (t.src = "about:blank", t.onload = null)
		}, 3E3)
	};
}
	(this, document));
