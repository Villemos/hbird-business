package org.hbird.business.configurator;
/**
 * 
 * Input channels;
 * - activemq:queue:queuedTasks (Commanding)
 * - activemq:queue:tasks (TaskExecutor).
 * - activemq:topic:orbitals (Navigation)
 * - activemq:topic:configuration (Navigation)
 * - activemq:topic:orbitals (Navigation)
 * - activemq:queue:requests (Navigation)
 * - activemq:queue:parameters (Limit)
 * - activemq:queue:injectedCommands (Commanding)
 * - activemq:queue:queuedCommands (Commanding)
 * - activemq:queue:releasedCommands (Commanding) 
 * 
 * Output channels;
 * - activemq:topic:parameters (TaskExecutor, SystemMonitor, Limit).
 * - activemq:queue:requests (Navigation)
 * - activemq:topic:orbitals (Navigation)
 * - activemq:queue:queuedCommands (Commanding)
 * - activemq:queue:releasedCommands (Commanding)
 * - activemq:topic:failedCommands (Commanding)
 * - activemq:topic:ejectedCommands (Commanding)
 * - activemq:queue:queuedTasks (Commanding)
 * - activemq:queue:tasks (Commanding)
 * */
