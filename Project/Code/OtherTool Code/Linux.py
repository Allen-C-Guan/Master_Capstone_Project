import paramiko
from stat import S_ISDIR
import os
'''
in this file, we could use ssh to download any file from Linux system.
'''

# 定义一个类，表示一台远端linux主机
class Linux(object):
    # 通过IP, 用户名，密码，超时时间初始化一个远程Linux主机
    def __init__(self, ip, username, password, timeout=30):
        self.ip = ip
        self.username = username
        self.password = password
        self.timeout = timeout
        # transport和chanel
        self.t = ''
        self.chan = ''
        # 链接失败的重试次数
        self.try_times = 3

    # 调用该方法连接远程主机
    def connect(self):
        pass
    # 断开连接
    def close(self):
        pass
    # 发送要执行的命令
    def send(self, cmd):
        pass
    # get单个文件
    def sftp_get(self, remotefile, localfile):
        t = paramiko.Transport(sock=(self.ip, 22))
        t.connect(username=self.username, password=self.password)
        sftp = paramiko.SFTPClient.from_transport(t)
        sftp.get(remotefile, localfile)
        t.close()

    # put单个文件
    def sftp_put(self, localfile, remotefile):
        t = paramiko.Transport(sock=(self.ip, 22))
        t.connect(username=self.username, password=self.password)
        sftp = paramiko.SFTPClient.from_transport(t)
        sftp.put(localfile, remotefile)
        t.close()

    def __get_all_files_in_remote_dir(self, sftp, remote_dir):
        # 保存文件夹文件的列表
        all_files = list()

        # 去掉最后的/如果有
        if remote_dir[-1] == "/":
            remote_dir = remote_dir[0:-1]

        # 获取目录下的所有文件
        files = sftp.listdir_attr(remote_dir)
        for x in files:
            # remote_dir目录中每一个文件或目录的完整路径
            filename = remote_dir + '/' + x.filename
            # 如果是目录，则递归处理该目录，
            # 这里用到了stat库中的S_ISDIR方法，与linux中的宏的名字完全一致
            if S_ISDIR(x.st_mode):
                all_files.extend(self.__get_all_files_in_remote_dir(sftp, filename))
            else:
                all_files.append(filename)
        return all_files

    def sftp_get_dir(self, remote_dir, local_dir):
        t = paramiko.Transport(sock=(self.ip, 22))
        t.connect(username=self.username, password=self.password)
        sftp = paramiko.SFTPClient.from_transport(t)

        # 获取远端linux主机上指定目录及其子目录下的所有文件
        all_files = self.__get_all_files_in_remote_dir(sftp, remote_dir)
        # 依次get每一个文件
        for x in all_files:
            filename = x.split('/')[-1]
            local_filename = os.path.join(local_dir, filename)
            print('loading file {}, please wait'.format(filename))
            sftp.get(x, local_filename)

    def sftp_remove_dir(self, remote_dir):
        t = paramiko.Transport(sock=(self.ip, 22))
        t.connect(username=self.username, password=self.password)
        sftp = paramiko.SFTPClient.from_transport(t)

        # 获取远端linux主机上指定目录及其子目录下的所有文件
        all_files = self.__get_all_files_in_remote_dir(sftp, remote_dir)
        # 依次remove每一个文件

        for x in all_files:
            filename = x.split('/')[-1]

            print('removing file {}, please wait'.format(filename))
            sftp.remove(x)