package io.github.daviddenton.finagle.aws

object S3Xml {

  def bucketContent(bucket: Bucket) =
    <Bucket>
      <Name>{bucket.name}</Name>
      <CreationDate>2016-10-14T09:16:04.000Z</CreationDate>
    </Bucket>

  def listBuckets(buckets: Bucket*) =
    <ListAllMyBucketsResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
      <Owner>
        <ID>25f3c384e613858c3d5bc830d0208aa3c7d2b62161c5fab525f8437ef2425e6a</ID>
        <DisplayName>name</DisplayName>
      </Owner>
      <Buckets>
        {buckets.map(bucketContent)}
      </Buckets>
    </ListAllMyBucketsResult>

  def keyContent(key: Key) =
    <Contents>
      <Key>{key.value}</Key>
      <LastModified>2017-02-16T15:14:15.000Z</LastModified>
      <ETag>
        &quot;
        a6a89ad48fdd67fe1cf7a5fccc83bb72
        &quot;
      </ETag>
      <Size>41</Size>
      <Owner>
        <ID>25f3c384e613858c3d5bc830d0208aa3c7d2b62161c5fab525f8437ef2425e6a</ID>
        <DisplayName>name</DisplayName>
      </Owner>
      <StorageClass>STANDARD</StorageClass>
    </Contents>

  def bucketContents(keys: Key*) = <ListBucketResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
    <Name>c52416d3-0d9f-47d3-80eb-47807cc88911</Name>
    <Prefix></Prefix>
    <Marker></Marker>
    <MaxKeys>1000</MaxKeys>
    <IsTruncated>false</IsTruncated>
    {keys.map(keyContent)}
  </ListBucketResult>

}
