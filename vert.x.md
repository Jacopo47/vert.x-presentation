---
marp: true
theme: default
_class: lead
paginate: true
backgroundImage: url('https://marp.app/assets/hero-background.svg')
---

![bg left:40% 80%](img/vertx.svg)

## Riciputi Jacopo
Reactive applications on the JVM
vertx.io


---

# What
Async programming on the JVM made easy.

# How
**Events**, events everywhere. Let Vert.x bring you in the **loop**

# Why
Threads are complex. Orchestrate them even more. 
But your service need to handle a lot events so you need them. 
***What if adding threads is not enough?*** 


---

# Blocking vs non-Blocking

### Performance's enemies: context-switching, I/O operations
![](img/blocking-threads.svg)



---

# Nodejs: a story of successes

* [Libuv](https://libuv.org/)
* [Event loop](https://nodejs.org/en/docs/guides/event-loop-timers-and-nexttick/)
* [Callback pattern](https://nodejs.org/en/knowledge/getting-started/control-flow/what-are-callbacks/)



```javascript
const fs = require('fs');
fs.readFile('/file.md', (err, data) => {
  if (err) throw err;
  console.log(data);
});
moreWork(); // will run before console.log
```

---

# Nodejs: Event-Loop analysis

```
   ┌───────────────────────────┐
┌─>│           timers          │
│  └─────────────┬─────────────┘
│  ┌─────────────┴─────────────┐
│  │     pending callbacks     │
│  └─────────────┬─────────────┘
│  ┌─────────────┴─────────────┐
│  │       idle, prepare       │
│  └─────────────┬─────────────┘      ┌───────────────┐
│  ┌─────────────┴─────────────┐      │   incoming:   │
│  │           poll            │<─────┤  connections, │
│  └─────────────┬─────────────┘      │   data, etc.  │
│  ┌─────────────┴─────────────┐      └───────────────┘
│  │           check           │
│  └─────────────┬─────────────┘
│  ┌─────────────┴─────────────┐
└──┤      close callbacks      │
   └───────────────────────────┘
```


---



# Vert.x

* Don't call us, we'll call you
   * a timer has fired
   * data IN from socket 
   * etc..
* Multi-Reactor Pattern, not just one thread
* Never block the event loop
* ***Futures*** are the future!


---

# Vert.x - Multi-Reactor Pattern

### Reactor pattern = 1 thread

By default Vert.x creates 2 * N_CORES event-loops. 

### How atomicity is guaranteed?

* Handler will never be executed concurrently;
* In most cases will always be called using the exact same event loop.

---

# Golden Rule - Don't block the event loop

Vert.x API are non-blocking, but handlers...

* Thread sleep;
* Waiting for a lock;
* Database query;
* Network call;
* Complex calculation;
* etc.. 

These operations will prevent other handlers to be consider for a *significant  amount of time*

Vert.x will help you in identify these cases through logs like: 

> Thread vertx-eventloop-thread-3 has been blocked for 20458 ms

---

# Futures

## Results - composition - coordination

