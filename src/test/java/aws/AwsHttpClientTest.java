package aws;

public class AwsHttpClientTest {

//    private final AuditTest.TestAuditor auditor = new AuditTest.TestAuditor();
//    private final AuditHandler auditHandler = new AuditHandler(new EmptyResponseHandler(), auditor);
//
//    private final AwsCredentialScope scope = awsCredentialScope("us-east", "s3");
//    private final AwsCredentials credentials = awsCredentials("access", "secret");
//
//    private final SettableClock clock = new SettableClock(Dates.date(2016, 1, 27, 15, 32, 50, 27));
//    private final AwsHttpClient client = new AwsHttpClient(auditHandler, clock, new AwsSignatureV4Signer(scope, credentials));
//
//    @Test
//    public void adds_authorization() throws Exception {
//        assertThat(
//                delegatedRequest(get("/test")).header("Authorization"),
//                is(some("AWS4-HMAC-SHA256 Credential=access/20160127/us-east/s3/aws4_request, SignedHeaders=content-length;x-amz-date, Signature=cfb15309d8787bd6879c2c01f805c2d6d648b3fd0719fe43647a6831fbce2774"))
//        );
//    }
//
//    @Test
//    public void adds_time_header() throws Exception {
//        assertThat(
//                delegatedRequest(get("/test")).header(AwsHeaders.DATE),
//                is(some("20160127T153250Z"))
//        );
//    }
//
//    @Test
//    public void adds_content_sha256() throws Exception {
//        assertThat(
//                delegatedRequest(get("/test")).header(AwsHeaders.CONTENT_SHA256),
//                is(some("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855"))
//        );
//    }
//
//    private Request delegatedRequest(final Request request) throws Exception {
//        return delegatedRequest(client, request);
//    }
//
//    private Request delegatedRequest(final AwsHttpClient client1, final Request request) throws Exception {
//        client1.handle(request);
//        return AuditTest.TestAuditor.receivedRequest;
//    }
}