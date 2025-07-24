Use **Postman** to run performance (perf) tests on your Journal API. The latest Postman desktop app includes built-in *
*performance testing features** that let you simulate real-world traffic by configuring multiple **virtual users**, test
durations, and ramp-up profiles. Key metrics—such as **response times, throughput (requests per second), and error rates
**—are displayed in real time in the Collection Runner interface.

**How to run a performance test in Postman:**

1. Use the **desktop app** (not the web version).
2. Organize your API requests into a Collection.
3. Open the Collection Runner, go to the **Performance** tab, and configure:

- Number of virtual users
- Test duration
- Load profile (fixed or ramp-up)

4. Run the test and monitor live metrics.
5. Export reports after the test if needed.

**Limitations:**

- All load is generated from your local machine—cloud-based distributed testing is not supported.
- The number of virtual users and scalability depend on your computer’s resources.
- Some advanced features and higher usage may require a paid plan.

For **basic API performance testing and bottleneck identification on small or medium workloads, Postman is a practical
and user-friendly option**. For high-scale, distributed, or production-grade testing, dedicated tools (like JMeter or
Gatling) offer more power and control.

**Summary:** Postman is suitable for small to moderate performance tests and provides a quick, visual, and accessible
way to simulate traffic and measure API responsiveness locally.
