import org.apache.kafka.common.errors.UnsupportedVersionException;
import org.apache.kafka.common.message.AddOffsetsToTxnRequestData;
import org.apache.kafka.common.message.AddOffsetsToTxnResponseData;
import org.apache.kafka.common.message.AddPartitionsToTxnRequestData;
import org.apache.kafka.common.message.AddPartitionsToTxnResponseData;
import org.apache.kafka.common.message.AlterClientQuotasRequestData;
import org.apache.kafka.common.message.AlterClientQuotasResponseData;
import org.apache.kafka.common.message.AlterConfigsRequestData;
import org.apache.kafka.common.message.AlterConfigsResponseData;
import org.apache.kafka.common.message.AlterPartitionReassignmentsRequestData;
import org.apache.kafka.common.message.AlterPartitionReassignmentsResponseData;
import org.apache.kafka.common.message.AlterReplicaLogDirsRequestData;
import org.apache.kafka.common.message.AlterReplicaLogDirsResponseData;
import org.apache.kafka.common.message.AlterUserScramCredentialsRequestData;
import org.apache.kafka.common.message.AlterUserScramCredentialsResponseData;
import org.apache.kafka.common.message.ApiMessageType;
import org.apache.kafka.common.message.ApiVersionsRequestData;
import org.apache.kafka.common.message.ApiVersionsResponseData;
import org.apache.kafka.common.message.BeginQuorumEpochRequestData;
import org.apache.kafka.common.message.BeginQuorumEpochResponseData;
import org.apache.kafka.common.message.BrokerHeartbeatRequestData;
import org.apache.kafka.common.message.BrokerHeartbeatResponseData;
import org.apache.kafka.common.message.BrokerRegistrationRequestData;
import org.apache.kafka.common.message.BrokerRegistrationResponseData;
import org.apache.kafka.common.message.ControlledShutdownRequestData;
import org.apache.kafka.common.message.ControlledShutdownResponseData;
import org.apache.kafka.common.message.CreateAclsRequestData;
import org.apache.kafka.common.message.CreateAclsResponseData;
import org.apache.kafka.common.message.CreateDelegationTokenRequestData;
import org.apache.kafka.common.message.CreateDelegationTokenResponseData;
import org.apache.kafka.common.message.CreatePartitionsRequestData;
import org.apache.kafka.common.message.CreatePartitionsResponseData;
import org.apache.kafka.common.message.CreateTopicsRequestData;
import org.apache.kafka.common.message.CreateTopicsResponseData;
import org.apache.kafka.common.message.DeleteAclsRequestData;
import org.apache.kafka.common.message.DeleteAclsResponseData;
import org.apache.kafka.common.message.DeleteGroupsRequestData;
import org.apache.kafka.common.message.DeleteGroupsResponseData;
import org.apache.kafka.common.message.DeleteRecordsRequestData;
import org.apache.kafka.common.message.DeleteRecordsResponseData;
import org.apache.kafka.common.message.DeleteTopicsRequestData;
import org.apache.kafka.common.message.DeleteTopicsResponseData;
import org.apache.kafka.common.message.DescribeAclsRequestData;
import org.apache.kafka.common.message.DescribeAclsResponseData;
import org.apache.kafka.common.message.DescribeClientQuotasRequestData;
import org.apache.kafka.common.message.DescribeClientQuotasResponseData;
import org.apache.kafka.common.message.DescribeClusterRequestData;
import org.apache.kafka.common.message.DescribeClusterResponseData;
import org.apache.kafka.common.message.DescribeConfigsRequestData;
import org.apache.kafka.common.message.DescribeConfigsResponseData;
import org.apache.kafka.common.message.DescribeDelegationTokenRequestData;
import org.apache.kafka.common.message.DescribeDelegationTokenResponseData;
import org.apache.kafka.common.message.DescribeGroupsRequestData;
import org.apache.kafka.common.message.DescribeGroupsResponseData;
import org.apache.kafka.common.message.DescribeLogDirsRequestData;
import org.apache.kafka.common.message.DescribeLogDirsResponseData;
import org.apache.kafka.common.message.DescribeProducersRequestData;
import org.apache.kafka.common.message.DescribeProducersResponseData;
import org.apache.kafka.common.message.DescribeQuorumRequestData;
import org.apache.kafka.common.message.DescribeQuorumResponseData;
import org.apache.kafka.common.message.DescribeTransactionsRequestData;
import org.apache.kafka.common.message.DescribeTransactionsResponseData;
import org.apache.kafka.common.message.DescribeUserScramCredentialsRequestData;
import org.apache.kafka.common.message.DescribeUserScramCredentialsResponseData;
import org.apache.kafka.common.message.ElectLeadersRequestData;
import org.apache.kafka.common.message.ElectLeadersResponseData;
import org.apache.kafka.common.message.EndQuorumEpochRequestData;
import org.apache.kafka.common.message.EndQuorumEpochResponseData;
import org.apache.kafka.common.message.EndTxnRequestData;
import org.apache.kafka.common.message.EndTxnResponseData;
import org.apache.kafka.common.message.EnvelopeRequestData;
import org.apache.kafka.common.message.EnvelopeResponseData;
import org.apache.kafka.common.message.ExpireDelegationTokenRequestData;
import org.apache.kafka.common.message.ExpireDelegationTokenResponseData;
import org.apache.kafka.common.message.FetchRequestData;
import org.apache.kafka.common.message.FetchResponseData;
import org.apache.kafka.common.message.FetchSnapshotRequestData;
import org.apache.kafka.common.message.FetchSnapshotResponseData;
import org.apache.kafka.common.message.FindCoordinatorRequestData;
import org.apache.kafka.common.message.FindCoordinatorResponseData;
import org.apache.kafka.common.message.HeartbeatRequestData;
import org.apache.kafka.common.message.HeartbeatResponseData;
import org.apache.kafka.common.message.IncrementalAlterConfigsRequestData;
import org.apache.kafka.common.message.IncrementalAlterConfigsResponseData;
import org.apache.kafka.common.message.InitProducerIdRequestData;
import org.apache.kafka.common.message.InitProducerIdResponseData;
import org.apache.kafka.common.message.JoinGroupRequestData;
import org.apache.kafka.common.message.JoinGroupResponseData;
import org.apache.kafka.common.message.LeaderAndIsrRequestData;
import org.apache.kafka.common.message.LeaderAndIsrResponseData;
import org.apache.kafka.common.message.LeaveGroupRequestData;
import org.apache.kafka.common.message.LeaveGroupResponseData;
import org.apache.kafka.common.message.ListGroupsRequestData;
import org.apache.kafka.common.message.ListGroupsResponseData;
import org.apache.kafka.common.message.ListOffsetsRequestData;
import org.apache.kafka.common.message.ListOffsetsResponseData;
import org.apache.kafka.common.message.ListPartitionReassignmentsRequestData;
import org.apache.kafka.common.message.ListPartitionReassignmentsResponseData;
import org.apache.kafka.common.message.ListTransactionsRequestData;
import org.apache.kafka.common.message.ListTransactionsResponseData;
import org.apache.kafka.common.message.MetadataRequestData;
import org.apache.kafka.common.message.MetadataResponseData;
import org.apache.kafka.common.message.OffsetCommitRequestData;
import org.apache.kafka.common.message.OffsetCommitResponseData;
import org.apache.kafka.common.message.OffsetDeleteRequestData;
import org.apache.kafka.common.message.OffsetDeleteResponseData;
import org.apache.kafka.common.message.OffsetFetchRequestData;
import org.apache.kafka.common.message.OffsetFetchResponseData;
import org.apache.kafka.common.message.OffsetForLeaderEpochRequestData;
import org.apache.kafka.common.message.OffsetForLeaderEpochResponseData;
import org.apache.kafka.common.message.ProduceRequestData;
import org.apache.kafka.common.message.ProduceResponseData;
import org.apache.kafka.common.message.RenewDelegationTokenRequestData;
import org.apache.kafka.common.message.RenewDelegationTokenResponseData;
import org.apache.kafka.common.message.SaslAuthenticateRequestData;
import org.apache.kafka.common.message.SaslAuthenticateResponseData;
import org.apache.kafka.common.message.SaslHandshakeRequestData;
import org.apache.kafka.common.message.SaslHandshakeResponseData;
import org.apache.kafka.common.message.StopReplicaRequestData;
import org.apache.kafka.common.message.StopReplicaResponseData;
import org.apache.kafka.common.message.SyncGroupRequestData;
import org.apache.kafka.common.message.SyncGroupResponseData;
import org.apache.kafka.common.message.TxnOffsetCommitRequestData;
import org.apache.kafka.common.message.TxnOffsetCommitResponseData;
import org.apache.kafka.common.message.UnregisterBrokerRequestData;
import org.apache.kafka.common.message.UnregisterBrokerResponseData;
import org.apache.kafka.common.message.UpdateFeaturesRequestData;
import org.apache.kafka.common.message.UpdateFeaturesResponseData;
import org.apache.kafka.common.message.UpdateMetadataRequestData;
import org.apache.kafka.common.message.UpdateMetadataResponseData;
import org.apache.kafka.common.message.VoteRequestData;
import org.apache.kafka.common.message.VoteResponseData;
import org.apache.kafka.common.message.WriteTxnMarkersRequestData;
import org.apache.kafka.common.message.WriteTxnMarkersResponseData;
import org.apache.kafka.common.protocol.ApiMessage;
import org.apache.kafka.common.protocol.types.Schema;

import java.util.EnumSet;

public class Enum {
    /*
     * Licensed to the Apache Software Foundation (ASF) under one or more
     * contributor license agreements. See the NOTICE file distributed with
     * this work for additional information regarding copyright ownership.
     * The ASF licenses this file to You under the Apache License, Version 2.0
     * (the "License"); you may not use this file except in compliance with
     * the License. You may obtain a copy of the License at
     *
     *    http://www.apache.org/licenses/LICENSE-2.0
     *
     * Unless required by applicable law or agreed to in writing, software
     * distributed under the License is distributed on an "AS IS" BASIS,
     * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     * See the License for the specific language governing permissions and
     * limitations under the License.
     */

    public static void main(String[] args) {
        System.out.println(ApiMessageType.values().length);
        for (ApiMessageType apiMessageType : ApiMessageType.values()) {
//            System.out.println("cache.add(new BrokerMetricTemplate(netWorkGroup, \"RequestMetrics-RequestsPerSec,request=" + apiMessageType.getName()+"\", \"b_" +apiMessageType.getName().toLowerCase()+
//                    "_requests_per_sec_one_minute_rate\", \"oneMinuteRate\", \"barad\", \"" +apiMessageType.getName()+"每秒请求数\"));");

            System.out.println("cache.add(new BrokerMetricTemplate(netWorkGroup, \"RequestMetrics-TotalTimeMs,request=" + apiMessageType.getName()+"\", \"b_" +apiMessageType.getName().toLowerCase()+
                    "_requests_total_times_max\", \"99thPercentile\", \"barad\", \"" +apiMessageType.getName()+"99分位耗时\"));");
            //System.out.println("RequestMetrics-TotalTimeMs,request="+);

        }
    }

    public enum ApiMessageType {
        PRODUCE("Produce", (short) 0, ProduceRequestData.SCHEMAS, ProduceResponseData.SCHEMAS, (short) 0, (short) 9, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER, org.apache.kafka.common.message.ApiMessageType.ListenerType.BROKER)),
        FETCH("Fetch", (short) 1, FetchRequestData.SCHEMAS, FetchResponseData.SCHEMAS, (short) 0, (short) 12, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER, org.apache.kafka.common.message.ApiMessageType.ListenerType.BROKER, org.apache.kafka.common.message.ApiMessageType.ListenerType.CONTROLLER)),
        LIST_OFFSETS("ListOffsets", (short) 2, ListOffsetsRequestData.SCHEMAS, ListOffsetsResponseData.SCHEMAS, (short) 0, (short) 6, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER, org.apache.kafka.common.message.ApiMessageType.ListenerType.BROKER)),
        METADATA("Metadata", (short) 3, MetadataRequestData.SCHEMAS, MetadataResponseData.SCHEMAS, (short) 0, (short) 11, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER, org.apache.kafka.common.message.ApiMessageType.ListenerType.BROKER, org.apache.kafka.common.message.ApiMessageType.ListenerType.CONTROLLER)),
        LEADER_AND_ISR("LeaderAndIsr", (short) 4, LeaderAndIsrRequestData.SCHEMAS, LeaderAndIsrResponseData.SCHEMAS, (short) 0, (short) 5, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER)),
        STOP_REPLICA("StopReplica", (short) 5, StopReplicaRequestData.SCHEMAS, StopReplicaResponseData.SCHEMAS, (short) 0, (short) 3, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER)),
        UPDATE_METADATA("UpdateMetadata", (short) 6, UpdateMetadataRequestData.SCHEMAS, UpdateMetadataResponseData.SCHEMAS, (short) 0, (short) 7, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER)),
        CONTROLLED_SHUTDOWN("ControlledShutdown", (short) 7, ControlledShutdownRequestData.SCHEMAS, ControlledShutdownResponseData.SCHEMAS, (short) 0, (short) 3, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER, org.apache.kafka.common.message.ApiMessageType.ListenerType.CONTROLLER)),
        OFFSET_COMMIT("OffsetCommit", (short) 8, OffsetCommitRequestData.SCHEMAS, OffsetCommitResponseData.SCHEMAS, (short) 0, (short) 8, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER, org.apache.kafka.common.message.ApiMessageType.ListenerType.BROKER)),
        OFFSET_FETCH("OffsetFetch", (short) 9, OffsetFetchRequestData.SCHEMAS, OffsetFetchResponseData.SCHEMAS, (short) 0, (short) 7, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER, org.apache.kafka.common.message.ApiMessageType.ListenerType.BROKER)),
        FIND_COORDINATOR("FindCoordinator", (short) 10, FindCoordinatorRequestData.SCHEMAS, FindCoordinatorResponseData.SCHEMAS, (short) 0, (short) 3, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER, org.apache.kafka.common.message.ApiMessageType.ListenerType.BROKER)),
        JOIN_GROUP("JoinGroup", (short) 11, JoinGroupRequestData.SCHEMAS, JoinGroupResponseData.SCHEMAS, (short) 0, (short) 7, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER, org.apache.kafka.common.message.ApiMessageType.ListenerType.BROKER)),
        HEARTBEAT("Heartbeat", (short) 12, HeartbeatRequestData.SCHEMAS, HeartbeatResponseData.SCHEMAS, (short) 0, (short) 4, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER, org.apache.kafka.common.message.ApiMessageType.ListenerType.BROKER)),
        LEAVE_GROUP("LeaveGroup", (short) 13, LeaveGroupRequestData.SCHEMAS, LeaveGroupResponseData.SCHEMAS, (short) 0, (short) 4, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER, org.apache.kafka.common.message.ApiMessageType.ListenerType.BROKER)),
        SYNC_GROUP("SyncGroup", (short) 14, SyncGroupRequestData.SCHEMAS, SyncGroupResponseData.SCHEMAS, (short) 0, (short) 5, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER, org.apache.kafka.common.message.ApiMessageType.ListenerType.BROKER)),
        DESCRIBE_GROUPS("DescribeGroups", (short) 15, DescribeGroupsRequestData.SCHEMAS, DescribeGroupsResponseData.SCHEMAS, (short) 0, (short) 5, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER, org.apache.kafka.common.message.ApiMessageType.ListenerType.BROKER)),
        LIST_GROUPS("ListGroups", (short) 16, ListGroupsRequestData.SCHEMAS, ListGroupsResponseData.SCHEMAS, (short) 0, (short) 4, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER, org.apache.kafka.common.message.ApiMessageType.ListenerType.BROKER)),
        SASL_HANDSHAKE("SaslHandshake", (short) 17, SaslHandshakeRequestData.SCHEMAS, SaslHandshakeResponseData.SCHEMAS, (short) 0, (short) 1, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER, org.apache.kafka.common.message.ApiMessageType.ListenerType.BROKER, org.apache.kafka.common.message.ApiMessageType.ListenerType.CONTROLLER)),
        API_VERSIONS("ApiVersions", (short) 18, ApiVersionsRequestData.SCHEMAS, ApiVersionsResponseData.SCHEMAS, (short) 0, (short) 3, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER, org.apache.kafka.common.message.ApiMessageType.ListenerType.BROKER, org.apache.kafka.common.message.ApiMessageType.ListenerType.CONTROLLER)),
        CREATE_TOPICS("CreateTopics", (short) 19, CreateTopicsRequestData.SCHEMAS, CreateTopicsResponseData.SCHEMAS, (short) 0, (short) 7, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER, org.apache.kafka.common.message.ApiMessageType.ListenerType.BROKER, org.apache.kafka.common.message.ApiMessageType.ListenerType.CONTROLLER)),
        DELETE_TOPICS("DeleteTopics", (short) 20, DeleteTopicsRequestData.SCHEMAS, DeleteTopicsResponseData.SCHEMAS, (short) 0, (short) 6, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER, org.apache.kafka.common.message.ApiMessageType.ListenerType.BROKER, org.apache.kafka.common.message.ApiMessageType.ListenerType.CONTROLLER)),
        DELETE_RECORDS("DeleteRecords", (short) 21, DeleteRecordsRequestData.SCHEMAS, DeleteRecordsResponseData.SCHEMAS, (short) 0, (short) 2, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER, org.apache.kafka.common.message.ApiMessageType.ListenerType.BROKER)),
        INIT_PRODUCER_ID("InitProducerId", (short) 22, InitProducerIdRequestData.SCHEMAS, InitProducerIdResponseData.SCHEMAS, (short) 0, (short) 4, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER)),
        OFFSET_FOR_LEADER_EPOCH("OffsetForLeaderEpoch", (short) 23, OffsetForLeaderEpochRequestData.SCHEMAS, OffsetForLeaderEpochResponseData.SCHEMAS, (short) 0, (short) 4, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER, org.apache.kafka.common.message.ApiMessageType.ListenerType.BROKER)),
        ADD_PARTITIONS_TO_TXN("AddPartitionsToTxn", (short) 24, AddPartitionsToTxnRequestData.SCHEMAS, AddPartitionsToTxnResponseData.SCHEMAS, (short) 0, (short) 3, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER)),
        ADD_OFFSETS_TO_TXN("AddOffsetsToTxn", (short) 25, AddOffsetsToTxnRequestData.SCHEMAS, AddOffsetsToTxnResponseData.SCHEMAS, (short) 0, (short) 3, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER)),
        END_TXN("EndTxn", (short) 26, EndTxnRequestData.SCHEMAS, EndTxnResponseData.SCHEMAS, (short) 0, (short) 3, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER)),
        WRITE_TXN_MARKERS("WriteTxnMarkers", (short) 27, WriteTxnMarkersRequestData.SCHEMAS, WriteTxnMarkersResponseData.SCHEMAS, (short) 0, (short) 1, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER, org.apache.kafka.common.message.ApiMessageType.ListenerType.BROKER)),
        TXN_OFFSET_COMMIT("TxnOffsetCommit", (short) 28, TxnOffsetCommitRequestData.SCHEMAS, TxnOffsetCommitResponseData.SCHEMAS, (short) 0, (short) 3, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER)),
        DESCRIBE_ACLS("DescribeAcls", (short) 29, DescribeAclsRequestData.SCHEMAS, DescribeAclsResponseData.SCHEMAS, (short) 0, (short) 2, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER)),
        CREATE_ACLS("CreateAcls", (short) 30, CreateAclsRequestData.SCHEMAS, CreateAclsResponseData.SCHEMAS, (short) 0, (short) 2, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER)),
        DELETE_ACLS("DeleteAcls", (short) 31, DeleteAclsRequestData.SCHEMAS, DeleteAclsResponseData.SCHEMAS, (short) 0, (short) 2, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER)),
        DESCRIBE_CONFIGS("DescribeConfigs", (short) 32, DescribeConfigsRequestData.SCHEMAS, DescribeConfigsResponseData.SCHEMAS, (short) 0, (short) 4, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER, org.apache.kafka.common.message.ApiMessageType.ListenerType.BROKER)),
        ALTER_CONFIGS("AlterConfigs", (short) 33, AlterConfigsRequestData.SCHEMAS, AlterConfigsResponseData.SCHEMAS, (short) 0, (short) 2, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER)),
        ALTER_REPLICA_LOG_DIRS("AlterReplicaLogDirs", (short) 34, AlterReplicaLogDirsRequestData.SCHEMAS, AlterReplicaLogDirsResponseData.SCHEMAS, (short) 0, (short) 2, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER, org.apache.kafka.common.message.ApiMessageType.ListenerType.BROKER)),
        DESCRIBE_LOG_DIRS("DescribeLogDirs", (short) 35, DescribeLogDirsRequestData.SCHEMAS, DescribeLogDirsResponseData.SCHEMAS, (short) 0, (short) 2, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER, org.apache.kafka.common.message.ApiMessageType.ListenerType.BROKER)),
        SASL_AUTHENTICATE("SaslAuthenticate", (short) 36, SaslAuthenticateRequestData.SCHEMAS, SaslAuthenticateResponseData.SCHEMAS, (short) 0, (short) 2, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER, org.apache.kafka.common.message.ApiMessageType.ListenerType.BROKER, org.apache.kafka.common.message.ApiMessageType.ListenerType.CONTROLLER)),
        CREATE_PARTITIONS("CreatePartitions", (short) 37, CreatePartitionsRequestData.SCHEMAS, CreatePartitionsResponseData.SCHEMAS, (short) 0, (short) 3, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER)),
        CREATE_DELEGATION_TOKEN("CreateDelegationToken", (short) 38, CreateDelegationTokenRequestData.SCHEMAS, CreateDelegationTokenResponseData.SCHEMAS, (short) 0, (short) 2, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER)),
        RENEW_DELEGATION_TOKEN("RenewDelegationToken", (short) 39, RenewDelegationTokenRequestData.SCHEMAS, RenewDelegationTokenResponseData.SCHEMAS, (short) 0, (short) 2, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER)),
        EXPIRE_DELEGATION_TOKEN("ExpireDelegationToken", (short) 40, ExpireDelegationTokenRequestData.SCHEMAS, ExpireDelegationTokenResponseData.SCHEMAS, (short) 0, (short) 2, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER)),
        DESCRIBE_DELEGATION_TOKEN("DescribeDelegationToken", (short) 41, DescribeDelegationTokenRequestData.SCHEMAS, DescribeDelegationTokenResponseData.SCHEMAS, (short) 0, (short) 2, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER)),
        DELETE_GROUPS("DeleteGroups", (short) 42, DeleteGroupsRequestData.SCHEMAS, DeleteGroupsResponseData.SCHEMAS, (short) 0, (short) 2, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER, org.apache.kafka.common.message.ApiMessageType.ListenerType.BROKER)),
        ELECT_LEADERS("ElectLeaders", (short) 43, ElectLeadersRequestData.SCHEMAS, ElectLeadersResponseData.SCHEMAS, (short) 0, (short) 2, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER)),
        INCREMENTAL_ALTER_CONFIGS("IncrementalAlterConfigs", (short) 44, IncrementalAlterConfigsRequestData.SCHEMAS, IncrementalAlterConfigsResponseData.SCHEMAS, (short) 0, (short) 1, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER, org.apache.kafka.common.message.ApiMessageType.ListenerType.BROKER, org.apache.kafka.common.message.ApiMessageType.ListenerType.CONTROLLER)),
        ALTER_PARTITION_REASSIGNMENTS("AlterPartitionReassignments", (short) 45, AlterPartitionReassignmentsRequestData.SCHEMAS, AlterPartitionReassignmentsResponseData.SCHEMAS, (short) 0, (short) 0, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER)),
        LIST_PARTITION_REASSIGNMENTS("ListPartitionReassignments", (short) 46, ListPartitionReassignmentsRequestData.SCHEMAS, ListPartitionReassignmentsResponseData.SCHEMAS, (short) 0, (short) 0, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER)),
        OFFSET_DELETE("OffsetDelete", (short) 47, OffsetDeleteRequestData.SCHEMAS, OffsetDeleteResponseData.SCHEMAS, (short) 0, (short) 0, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER, org.apache.kafka.common.message.ApiMessageType.ListenerType.BROKER)),
        DESCRIBE_CLIENT_QUOTAS("DescribeClientQuotas", (short) 48, DescribeClientQuotasRequestData.SCHEMAS, DescribeClientQuotasResponseData.SCHEMAS, (short) 0, (short) 1, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER)),
        ALTER_CLIENT_QUOTAS("AlterClientQuotas", (short) 49, AlterClientQuotasRequestData.SCHEMAS, AlterClientQuotasResponseData.SCHEMAS, (short) 0, (short) 1, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER, org.apache.kafka.common.message.ApiMessageType.ListenerType.BROKER, org.apache.kafka.common.message.ApiMessageType.ListenerType.CONTROLLER)),
        DESCRIBE_USER_SCRAM_CREDENTIALS("DescribeUserScramCredentials", (short) 50, DescribeUserScramCredentialsRequestData.SCHEMAS, DescribeUserScramCredentialsResponseData.SCHEMAS, (short) 0, (short) 0, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER)),
        ALTER_USER_SCRAM_CREDENTIALS("AlterUserScramCredentials", (short) 51, AlterUserScramCredentialsRequestData.SCHEMAS, AlterUserScramCredentialsResponseData.SCHEMAS, (short) 0, (short) 0, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER)),
        VOTE("Vote", (short) 52, VoteRequestData.SCHEMAS, VoteResponseData.SCHEMAS, (short) 0, (short) 0, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.CONTROLLER)),
        BEGIN_QUORUM_EPOCH("BeginQuorumEpoch", (short) 53, BeginQuorumEpochRequestData.SCHEMAS, BeginQuorumEpochResponseData.SCHEMAS, (short) 0, (short) 0, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.CONTROLLER)),
        END_QUORUM_EPOCH("EndQuorumEpoch", (short) 54, EndQuorumEpochRequestData.SCHEMAS, EndQuorumEpochResponseData.SCHEMAS, (short) 0, (short) 0, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.CONTROLLER)),
        DESCRIBE_QUORUM("DescribeQuorum", (short) 55, DescribeQuorumRequestData.SCHEMAS, DescribeQuorumResponseData.SCHEMAS, (short) 0, (short) 0, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.BROKER, org.apache.kafka.common.message.ApiMessageType.ListenerType.CONTROLLER)),
        ALTER_ISR("AlterIsr", (short) 56, DescribeQuorumRequestData.SCHEMAS, DescribeQuorumRequestData.SCHEMAS, (short) 0, (short) 0, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER, org.apache.kafka.common.message.ApiMessageType.ListenerType.CONTROLLER)),
        UPDATE_FEATURES("UpdateFeatures", (short) 57, UpdateFeaturesRequestData.SCHEMAS, UpdateFeaturesResponseData.SCHEMAS, (short) 0, (short) 0, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER)),
        ENVELOPE("Envelope", (short) 58, EnvelopeRequestData.SCHEMAS, EnvelopeResponseData.SCHEMAS, (short) 0, (short) 0, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.CONTROLLER)),
        FETCH_SNAPSHOT("FetchSnapshot", (short) 59, FetchSnapshotRequestData.SCHEMAS, FetchSnapshotResponseData.SCHEMAS, (short) 0, (short) 0, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.CONTROLLER)),
        DESCRIBE_CLUSTER("DescribeCluster", (short) 60, DescribeClusterRequestData.SCHEMAS, DescribeClusterResponseData.SCHEMAS, (short) 0, (short) 0, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER, org.apache.kafka.common.message.ApiMessageType.ListenerType.BROKER)),
        DESCRIBE_PRODUCERS("DescribeProducers", (short) 61, DescribeProducersRequestData.SCHEMAS, DescribeProducersResponseData.SCHEMAS, (short) 0, (short) 0, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER, org.apache.kafka.common.message.ApiMessageType.ListenerType.BROKER)),
        BROKER_REGISTRATION("BrokerRegistration", (short) 62, BrokerRegistrationRequestData.SCHEMAS, BrokerRegistrationResponseData.SCHEMAS, (short) 0, (short) 0, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.CONTROLLER)),
        BROKER_HEARTBEAT("BrokerHeartbeat", (short) 63, BrokerHeartbeatRequestData.SCHEMAS, BrokerHeartbeatResponseData.SCHEMAS, (short) 0, (short) 0, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.CONTROLLER)),
        UNREGISTER_BROKER("UnregisterBroker", (short) 64, UnregisterBrokerRequestData.SCHEMAS, UnregisterBrokerResponseData.SCHEMAS, (short) 0, (short) 0, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.BROKER, org.apache.kafka.common.message.ApiMessageType.ListenerType.CONTROLLER)),
        DESCRIBE_TRANSACTIONS("DescribeTransactions", (short) 65, DescribeTransactionsRequestData.SCHEMAS, DescribeTransactionsResponseData.SCHEMAS, (short) 0, (short) 0, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER, org.apache.kafka.common.message.ApiMessageType.ListenerType.BROKER)),
        LIST_TRANSACTIONS("ListTransactions", (short) 66, ListTransactionsRequestData.SCHEMAS, ListTransactionsResponseData.SCHEMAS, (short) 0, (short) 0, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER, org.apache.kafka.common.message.ApiMessageType.ListenerType.BROKER)),
        DESCRIBE_REMOTE_METADATA("DescribeRemoteMetadata", (short) 99, DescribeQuorumRequestData.SCHEMAS, DescribeQuorumRequestData.SCHEMAS, (short) 0, (short) 1, EnumSet.of(org.apache.kafka.common.message.ApiMessageType.ListenerType.ZK_BROKER, org.apache.kafka.common.message.ApiMessageType.ListenerType.BROKER));

        public final String name;
        private final short apiKey;
        private final Schema[] requestSchemas;
        private final Schema[] responseSchemas;
        private final short lowestSupportedVersion;
        private final short highestSupportedVersion;
        private final EnumSet<org.apache.kafka.common.message.ApiMessageType.ListenerType> listeners;

        ApiMessageType(String name, short apiKey, Schema[] requestSchemas, Schema[] responseSchemas, short lowestSupportedVersion, short highestSupportedVersion, EnumSet<org.apache.kafka.common.message.ApiMessageType.ListenerType> listeners) {
            this.name = name;
            this.apiKey = apiKey;
            this.requestSchemas = requestSchemas;
            this.responseSchemas = responseSchemas;
            this.lowestSupportedVersion = lowestSupportedVersion;
            this.highestSupportedVersion = highestSupportedVersion;
            this.listeners = listeners;
        }


        public String getName(){
            return this.name;
        }



        public short lowestSupportedVersion() {
            return this.lowestSupportedVersion;
        }

        public short highestSupportedVersion() {
            return this.highestSupportedVersion;
        }

        public EnumSet<org.apache.kafka.common.message.ApiMessageType.ListenerType> listeners() {
            return this.listeners;
        }

        public short apiKey() {
            return this.apiKey;
        }

        public Schema[] requestSchemas() {
            return this.requestSchemas;
        }

        public Schema[] responseSchemas() {
            return this.responseSchemas;
        }

        @Override
        public String toString() {
            return this.name();
        }

        public short requestHeaderVersion(short _version) {
            switch (apiKey) {
                case 0: // Produce
                    if (_version >= 9) {
                        return (short) 2;
                    } else {
                        return (short) 1;
                    }
                case 1: // Fetch
                    if (_version >= 12) {
                        return (short) 2;
                    } else {
                        return (short) 1;
                    }
                case 2: // ListOffsets
                    if (_version >= 6) {
                        return (short) 2;
                    } else {
                        return (short) 1;
                    }
                case 3: // Metadata
                    if (_version >= 9) {
                        return (short) 2;
                    } else {
                        return (short) 1;
                    }
                case 4: // LeaderAndIsr
                    if (_version >= 4) {
                        return (short) 2;
                    } else {
                        return (short) 1;
                    }
                case 5: // StopReplica
                    if (_version >= 2) {
                        return (short) 2;
                    } else {
                        return (short) 1;
                    }
                case 6: // UpdateMetadata
                    if (_version >= 6) {
                        return (short) 2;
                    } else {
                        return (short) 1;
                    }
                case 7: // ControlledShutdown
                    // Version 0 of ControlledShutdownRequest has a non-standard request header
                    // which does not include clientId.  Version 1 of ControlledShutdownRequest
                    // and later use the standard request header.
                    if (_version == 0) {
                        return (short) 0;
                    }
                    if (_version >= 3) {
                        return (short) 2;
                    } else {
                        return (short) 1;
                    }
                case 8: // OffsetCommit
                    if (_version >= 8) {
                        return (short) 2;
                    } else {
                        return (short) 1;
                    }
                case 9: // OffsetFetch
                    if (_version >= 6) {
                        return (short) 2;
                    } else {
                        return (short) 1;
                    }
                case 10: // FindCoordinator
                    if (_version >= 3) {
                        return (short) 2;
                    } else {
                        return (short) 1;
                    }
                case 11: // JoinGroup
                    if (_version >= 6) {
                        return (short) 2;
                    } else {
                        return (short) 1;
                    }
                case 12: // Heartbeat
                    if (_version >= 4) {
                        return (short) 2;
                    } else {
                        return (short) 1;
                    }
                case 13: // LeaveGroup
                    if (_version >= 4) {
                        return (short) 2;
                    } else {
                        return (short) 1;
                    }
                case 14: // SyncGroup
                    if (_version >= 4) {
                        return (short) 2;
                    } else {
                        return (short) 1;
                    }
                case 15: // DescribeGroups
                    if (_version >= 5) {
                        return (short) 2;
                    } else {
                        return (short) 1;
                    }
                case 16: // ListGroups
                    if (_version >= 3) {
                        return (short) 2;
                    } else {
                        return (short) 1;
                    }
                case 17: // SaslHandshake
                    return (short) 1;
                case 18: // ApiVersions
                    if (_version >= 3) {
                        return (short) 2;
                    } else {
                        return (short) 1;
                    }
                case 19: // CreateTopics
                    if (_version >= 5) {
                        return (short) 2;
                    } else {
                        return (short) 1;
                    }
                case 20: // DeleteTopics
                    if (_version >= 4) {
                        return (short) 2;
                    } else {
                        return (short) 1;
                    }
                case 21: // DeleteRecords
                    if (_version >= 2) {
                        return (short) 2;
                    } else {
                        return (short) 1;
                    }
                case 22: // InitProducerId
                    if (_version >= 2) {
                        return (short) 2;
                    } else {
                        return (short) 1;
                    }
                case 23: // OffsetForLeaderEpoch
                    if (_version >= 4) {
                        return (short) 2;
                    } else {
                        return (short) 1;
                    }
                case 24: // AddPartitionsToTxn
                    if (_version >= 3) {
                        return (short) 2;
                    } else {
                        return (short) 1;
                    }
                case 25: // AddOffsetsToTxn
                    if (_version >= 3) {
                        return (short) 2;
                    } else {
                        return (short) 1;
                    }
                case 26: // EndTxn
                    if (_version >= 3) {
                        return (short) 2;
                    } else {
                        return (short) 1;
                    }
                case 27: // WriteTxnMarkers
                    if (_version >= 1) {
                        return (short) 2;
                    } else {
                        return (short) 1;
                    }
                case 28: // TxnOffsetCommit
                    if (_version >= 3) {
                        return (short) 2;
                    } else {
                        return (short) 1;
                    }
                case 29: // DescribeAcls
                    if (_version >= 2) {
                        return (short) 2;
                    } else {
                        return (short) 1;
                    }
                case 30: // CreateAcls
                    if (_version >= 2) {
                        return (short) 2;
                    } else {
                        return (short) 1;
                    }
                case 31: // DeleteAcls
                    if (_version >= 2) {
                        return (short) 2;
                    } else {
                        return (short) 1;
                    }
                case 32: // DescribeConfigs
                    if (_version >= 4) {
                        return (short) 2;
                    } else {
                        return (short) 1;
                    }
                case 33: // AlterConfigs
                    if (_version >= 2) {
                        return (short) 2;
                    } else {
                        return (short) 1;
                    }
                case 34: // AlterReplicaLogDirs
                    if (_version >= 2) {
                        return (short) 2;
                    } else {
                        return (short) 1;
                    }
                case 35: // DescribeLogDirs
                    if (_version >= 2) {
                        return (short) 2;
                    } else {
                        return (short) 1;
                    }
                case 36: // SaslAuthenticate
                    if (_version >= 2) {
                        return (short) 2;
                    } else {
                        return (short) 1;
                    }
                case 37: // CreatePartitions
                    if (_version >= 2) {
                        return (short) 2;
                    } else {
                        return (short) 1;
                    }
                case 38: // CreateDelegationToken
                    if (_version >= 2) {
                        return (short) 2;
                    } else {
                        return (short) 1;
                    }
                case 39: // RenewDelegationToken
                    if (_version >= 2) {
                        return (short) 2;
                    } else {
                        return (short) 1;
                    }
                case 40: // ExpireDelegationToken
                    if (_version >= 2) {
                        return (short) 2;
                    } else {
                        return (short) 1;
                    }
                case 41: // DescribeDelegationToken
                    if (_version >= 2) {
                        return (short) 2;
                    } else {
                        return (short) 1;
                    }
                case 42: // DeleteGroups
                    if (_version >= 2) {
                        return (short) 2;
                    } else {
                        return (short) 1;
                    }
                case 43: // ElectLeaders
                    if (_version >= 2) {
                        return (short) 2;
                    } else {
                        return (short) 1;
                    }
                case 44: // IncrementalAlterConfigs
                    if (_version >= 1) {
                        return (short) 2;
                    } else {
                        return (short) 1;
                    }
                case 45: // AlterPartitionReassignments
                    return (short) 2;
                case 46: // ListPartitionReassignments
                    return (short) 2;
                case 47: // OffsetDelete
                    return (short) 1;
                case 48: // DescribeClientQuotas
                    if (_version >= 1) {
                        return (short) 2;
                    } else {
                        return (short) 1;
                    }
                case 49: // AlterClientQuotas
                    if (_version >= 1) {
                        return (short) 2;
                    } else {
                        return (short) 1;
                    }
                case 50: // DescribeUserScramCredentials
                    return (short) 2;
                case 51: // AlterUserScramCredentials
                    return (short) 2;
                case 52: // Vote
                    return (short) 2;
                case 53: // BeginQuorumEpoch
                    return (short) 1;
                case 54: // EndQuorumEpoch
                    return (short) 1;
                case 55: // DescribeQuorum
                    return (short) 2;
                case 56: // AlterIsr
                    return (short) 2;
                case 57: // UpdateFeatures
                    return (short) 2;
                case 58: // Envelope
                    return (short) 2;
                case 59: // FetchSnapshot
                    return (short) 2;
                case 60: // DescribeCluster
                    return (short) 2;
                case 61: // DescribeProducers
                    return (short) 2;
                case 62: // BrokerRegistration
                    return (short) 2;
                case 63: // BrokerHeartbeat
                    return (short) 2;
                case 64: // UnregisterBroker
                    return (short) 2;
                case 65: // DescribeTransactions
                    return (short) 2;
                case 66: // ListTransactions
                    return (short) 2;
                case 99: // DescribeRemoteMetadata
                    return (short) 2;
                default:
                    throw new UnsupportedVersionException("Unsupported API key " + apiKey);
            }
        }

        public short responseHeaderVersion(short _version) {
            switch (apiKey) {
                case 0: // Produce
                    if (_version >= 9) {
                        return (short) 1;
                    } else {
                        return (short) 0;
                    }
                case 1: // Fetch
                    if (_version >= 12) {
                        return (short) 1;
                    } else {
                        return (short) 0;
                    }
                case 2: // ListOffsets
                    if (_version >= 6) {
                        return (short) 1;
                    } else {
                        return (short) 0;
                    }
                case 3: // Metadata
                    if (_version >= 9) {
                        return (short) 1;
                    } else {
                        return (short) 0;
                    }
                case 4: // LeaderAndIsr
                    if (_version >= 4) {
                        return (short) 1;
                    } else {
                        return (short) 0;
                    }
                case 5: // StopReplica
                    if (_version >= 2) {
                        return (short) 1;
                    } else {
                        return (short) 0;
                    }
                case 6: // UpdateMetadata
                    if (_version >= 6) {
                        return (short) 1;
                    } else {
                        return (short) 0;
                    }
                case 7: // ControlledShutdown
                    if (_version >= 3) {
                        return (short) 1;
                    } else {
                        return (short) 0;
                    }
                case 8: // OffsetCommit
                    if (_version >= 8) {
                        return (short) 1;
                    } else {
                        return (short) 0;
                    }
                case 9: // OffsetFetch
                    if (_version >= 6) {
                        return (short) 1;
                    } else {
                        return (short) 0;
                    }
                case 10: // FindCoordinator
                    if (_version >= 3) {
                        return (short) 1;
                    } else {
                        return (short) 0;
                    }
                case 11: // JoinGroup
                    if (_version >= 6) {
                        return (short) 1;
                    } else {
                        return (short) 0;
                    }
                case 12: // Heartbeat
                    if (_version >= 4) {
                        return (short) 1;
                    } else {
                        return (short) 0;
                    }
                case 13: // LeaveGroup
                    if (_version >= 4) {
                        return (short) 1;
                    } else {
                        return (short) 0;
                    }
                case 14: // SyncGroup
                    if (_version >= 4) {
                        return (short) 1;
                    } else {
                        return (short) 0;
                    }
                case 15: // DescribeGroups
                    if (_version >= 5) {
                        return (short) 1;
                    } else {
                        return (short) 0;
                    }
                case 16: // ListGroups
                    if (_version >= 3) {
                        return (short) 1;
                    } else {
                        return (short) 0;
                    }
                case 17: // SaslHandshake
                    return (short) 0;
                case 18: // ApiVersions
                    // ApiVersionsResponse always includes a v0 header.
                    // See KIP-511 for details.
                    return (short) 0;
                case 19: // CreateTopics
                    if (_version >= 5) {
                        return (short) 1;
                    } else {
                        return (short) 0;
                    }
                case 20: // DeleteTopics
                    if (_version >= 4) {
                        return (short) 1;
                    } else {
                        return (short) 0;
                    }
                case 21: // DeleteRecords
                    if (_version >= 2) {
                        return (short) 1;
                    } else {
                        return (short) 0;
                    }
                case 22: // InitProducerId
                    if (_version >= 2) {
                        return (short) 1;
                    } else {
                        return (short) 0;
                    }
                case 23: // OffsetForLeaderEpoch
                    if (_version >= 4) {
                        return (short) 1;
                    } else {
                        return (short) 0;
                    }
                case 24: // AddPartitionsToTxn
                    if (_version >= 3) {
                        return (short) 1;
                    } else {
                        return (short) 0;
                    }
                case 25: // AddOffsetsToTxn
                    if (_version >= 3) {
                        return (short) 1;
                    } else {
                        return (short) 0;
                    }
                case 26: // EndTxn
                    if (_version >= 3) {
                        return (short) 1;
                    } else {
                        return (short) 0;
                    }
                case 27: // WriteTxnMarkers
                    if (_version >= 1) {
                        return (short) 1;
                    } else {
                        return (short) 0;
                    }
                case 28: // TxnOffsetCommit
                    if (_version >= 3) {
                        return (short) 1;
                    } else {
                        return (short) 0;
                    }
                case 29: // DescribeAcls
                    if (_version >= 2) {
                        return (short) 1;
                    } else {
                        return (short) 0;
                    }
                case 30: // CreateAcls
                    if (_version >= 2) {
                        return (short) 1;
                    } else {
                        return (short) 0;
                    }
                case 31: // DeleteAcls
                    if (_version >= 2) {
                        return (short) 1;
                    } else {
                        return (short) 0;
                    }
                case 32: // DescribeConfigs
                    if (_version >= 4) {
                        return (short) 1;
                    } else {
                        return (short) 0;
                    }
                case 33: // AlterConfigs
                    if (_version >= 2) {
                        return (short) 1;
                    } else {
                        return (short) 0;
                    }
                case 34: // AlterReplicaLogDirs
                    if (_version >= 2) {
                        return (short) 1;
                    } else {
                        return (short) 0;
                    }
                case 35: // DescribeLogDirs
                    if (_version >= 2) {
                        return (short) 1;
                    } else {
                        return (short) 0;
                    }
                case 36: // SaslAuthenticate
                    if (_version >= 2) {
                        return (short) 1;
                    } else {
                        return (short) 0;
                    }
                case 37: // CreatePartitions
                    if (_version >= 2) {
                        return (short) 1;
                    } else {
                        return (short) 0;
                    }
                case 38: // CreateDelegationToken
                    if (_version >= 2) {
                        return (short) 1;
                    } else {
                        return (short) 0;
                    }
                case 39: // RenewDelegationToken
                    if (_version >= 2) {
                        return (short) 1;
                    } else {
                        return (short) 0;
                    }
                case 40: // ExpireDelegationToken
                    if (_version >= 2) {
                        return (short) 1;
                    } else {
                        return (short) 0;
                    }
                case 41: // DescribeDelegationToken
                    if (_version >= 2) {
                        return (short) 1;
                    } else {
                        return (short) 0;
                    }
                case 42: // DeleteGroups
                    if (_version >= 2) {
                        return (short) 1;
                    } else {
                        return (short) 0;
                    }
                case 43: // ElectLeaders
                    if (_version >= 2) {
                        return (short) 1;
                    } else {
                        return (short) 0;
                    }
                case 44: // IncrementalAlterConfigs
                    if (_version >= 1) {
                        return (short) 1;
                    } else {
                        return (short) 0;
                    }
                case 45: // AlterPartitionReassignments
                    return (short) 1;
                case 46: // ListPartitionReassignments
                    return (short) 1;
                case 47: // OffsetDelete
                    return (short) 0;
                case 48: // DescribeClientQuotas
                    if (_version >= 1) {
                        return (short) 1;
                    } else {
                        return (short) 0;
                    }
                case 49: // AlterClientQuotas
                    if (_version >= 1) {
                        return (short) 1;
                    } else {
                        return (short) 0;
                    }
                case 50: // DescribeUserScramCredentials
                    return (short) 1;
                case 51: // AlterUserScramCredentials
                    return (short) 1;
                case 52: // Vote
                    return (short) 1;
                case 53: // BeginQuorumEpoch
                    return (short) 0;
                case 54: // EndQuorumEpoch
                    return (short) 0;
                case 55: // DescribeQuorum
                    return (short) 1;
                case 56: // AlterIsr
                    return (short) 1;
                case 57: // UpdateFeatures
                    return (short) 1;
                case 58: // Envelope
                    return (short) 1;
                case 59: // FetchSnapshot
                    return (short) 1;
                case 60: // DescribeCluster
                    return (short) 1;
                case 61: // DescribeProducers
                    return (short) 1;
                case 62: // BrokerRegistration
                    return (short) 1;
                case 63: // BrokerHeartbeat
                    return (short) 1;
                case 64: // UnregisterBroker
                    return (short) 1;
                case 65: // DescribeTransactions
                    return (short) 1;
                case 66: // ListTransactions
                    return (short) 1;
                case 99: // DescribeRemoteMetadata
                    return (short) 1;
                default:
                    throw new UnsupportedVersionException("Unsupported API key " + apiKey);
            }
        }

        public enum ListenerType {
            ZK_BROKER,
            BROKER,
            CONTROLLER;
        }

    }

}
